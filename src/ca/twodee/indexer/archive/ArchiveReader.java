package ca.twodee.indexer.archive;

import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * A class to read an archive and extract the first image.
 * TODO: refactor process to not be called by the constructor
 */
public class ArchiveReader implements AutoCloseable {
	private final static Logger logger = Logger.getLogger(ArchiveReader.class);
	private final static Long MAX_IMAGE_SIZE_LIMIT_BYTES = 26214400L; // 25MB

	private final ISevenZipInArchive archive;
	private final RandomAccessFile raf;
	private Integer imageCount = -1;
	private ISimpleInArchiveItem firstImageItem = null;
	
	/**
	 * Opens an archive and processes it, looking for the first image. Refer to {@link process}.
	 */
	public ArchiveReader(String path) throws FileNotFoundException, SevenZipException {
		raf = new RandomAccessFile(path, "r");
        RandomAccessFileInStream rafis = new RandomAccessFileInStream(raf);
        
        archive = SevenZip.openInArchive(null, rafis);
        
        this.process();
	}
	
	public void close() {
		if (archive != null) {
			try {
				archive.close();
			} catch (SevenZipException e) {
				logger.debug(e);
			}
		}

		IOUtils.closeQuietly(raf);
	}
	
	/**
	 * Iterates over archive and produces:
	 * - reference to first image
	 * - image count
	 */
	private void process() throws SevenZipException {
		ISimpleInArchive archiveSimple = archive.getSimpleInterface();
		ISimpleInArchiveItem[] items = archiveSimple.getArchiveItems();
		
		ISimpleInArchiveItem first = null;
		String firstName = null;
		Integer imageCount = 0;
		
		// linear search for first lexicographic name
		for (ISimpleInArchiveItem candidate : items) {
			String candidatePath = candidate.getPath();
			
			// no encrypted files or folders
			if (candidate.isEncrypted() || candidate.isFolder()) {
				continue;
			}
			
			if (candidatePath.endsWith("jpg") || candidatePath.endsWith("png")) {
				imageCount++;
				if (firstName == null || firstName.toLowerCase().compareTo(candidatePath.toLowerCase()) > 0) {
					first = candidate;
					firstName = candidatePath;
				}
			}
		}
		
		this.imageCount = imageCount;
		this.firstImageItem = first;
	}
	
	public byte[] getFirstImage() throws SevenZipException {       
        // check packed size, encryption before extraction
        if (firstImageItem == null || firstImageItem.getSize() > MAX_IMAGE_SIZE_LIMIT_BYTES) {
        	return null;
        }
        
        InMemorySequentialOutStream imsos = new InMemorySequentialOutStream();
		firstImageItem.extractSlow(imsos);
		return imsos.getBytes();
	}

	public Integer getImageCount() {
		return imageCount;
	}

}
