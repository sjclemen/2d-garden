package ca.twodee.indexer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.log4j.Logger;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.backend.manager.BackendListenableFutureTask;
import ca.twodee.backend.manager.BackendListenableFutureTask.BackendTaskPriority;
import ca.twodee.indexer.archive.ArchiveReader;
import ca.twodee.indexer.archive.Thumbnailer;
import ca.twodee.indexer.crawler.FilesystemCrawler;
import ca.twodee.persistence.message.archive.AddArchiveRequest;
import ca.twodee.persistence.message.archive.AddOrRetrieveArchiveRepositoryRequest;
import ca.twodee.persistence.message.archive.GetArchivesRequest;
import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveRepository;
import ca.twodee.schema.ArchiveThumbnail;

// TODO: rewrite this to provide incremental progress
public class Indexer {
	private final static Logger logger = Logger.getLogger(Indexer.class);
	private final static String[] EXTENSION_ARRAY = {"rar", "zip", "cbz", "cbr", "7z", "tar"};
	private final Path path;
	private final BackendConnection conn;
	
	public Indexer(BackendConnection connection, String pathStr) {
		this.path = FileSystems.getDefault().getPath(pathStr).normalize().toAbsolutePath();
		this.conn = connection;
	}

	public void indexPath() {
		// TBD: provide method to determine if indexing process is complete, to serialize requests
		String pathStr = this.path.toString();
		AddOrRetrieveArchiveRepositoryRequest request = new AddOrRetrieveArchiveRepositoryRequest(pathStr);
		BackendListenableFutureTask<ArchiveRepository> future = conn.enqueueTask(request);
		try {
			ArchiveRepository repoResponse = future.get();
			List <Archive> archives = getArchivesInRepository(repoResponse);
			handleArchiveList(repoResponse, archives);
		} catch (Exception e) {
			logger.error("Error in DB operations while crawling: " + e.getMessage(), e);
		}
	}
	
	private List <Archive> getArchivesInRepository(ArchiveRepository archiveRepository) throws Exception {
		GetArchivesRequest request = new GetArchivesRequest(archiveRepository.getId());
		BackendListenableFutureTask<List<Archive>> future = conn.enqueueTask(request);
		return future.get();
	}
	
	private void handleArchiveList(ArchiveRepository archiveRepository, List <Archive> archives) {
		try {
			HashMap <Path, Long> onDisk = crawlDisk();
			HashMap <Path, Archive> inDatabase = archivesToPathArchiveHashMap(archiveRepository, archives);
			
			HashMap <Path, Long> onlyOnDisk = new HashMap <>();
			HashMap <Path, Archive> rescan = new HashMap<>();
			
			// we want three groupings: on disk, in database, both.
			// for those in both, there's no state transition, whereas we need to handle
			// adding or files going missing in the case of only on disk or in database
			
			Set<Entry<Path, Long>> diskItems = onDisk.entrySet();
			Iterator<Entry<Path, Long>> diskItemsIterator = diskItems.iterator();
			while (diskItemsIterator.hasNext()) {
				Entry<Path, Long> diskEntry = diskItemsIterator.next();
				Archive dbArchive = inDatabase.get(diskEntry.getKey());
				if (dbArchive == null) {
					// does not exist in database, or has been moved.
					onlyOnDisk.put(diskEntry.getKey(), diskEntry.getValue());
					diskItemsIterator.remove();
				} else if (dbArchive.getLastModified() == diskEntry.getValue()) {
					// remove it from further consideration, because the database and filesystem line up
					// TBD: if has missing, unmark as missing
					inDatabase.remove(diskEntry.getKey());
					diskItemsIterator.remove();
				} else {
					// non-matching timestamps
					// we need to rescan the item and make sure the hash still matches
					rescan.put(diskEntry.getKey(), dbArchive);
					inDatabase.remove(diskEntry.getKey());
					diskItemsIterator.remove();
				}
			}
			
			persistNewArchives(onlyOnDisk, archiveRepository);
			
			logUnprocessedItems("Missing archive, in DB but not on disk: ", inDatabase);
			logUnprocessedItems("Requires rescan, but not supported: ", rescan);
			
		} catch (IOException e) {
			logger.error("Failed to handle archive list due to " + e.getMessage(), e);
		}
	}
	
	private void logUnprocessedItems(String message, HashMap <Path, Archive> items) {
		Iterator<Entry<Path, Archive>> i = items.entrySet().iterator();
		while (i.hasNext()) {
			Entry<Path, Archive> entry = i.next();
			Path p = entry.getKey();
			Archive a = entry.getValue();
			logger.info(message + " " + p.toString() + " " + a.getId());
		}
	}
	
	private void persistNewArchives(HashMap <Path, Long> paths, ArchiveRepository repo) {
		Iterator<Entry<Path, Long>> iterator = paths.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Path, Long> item = iterator.next();
			String absolutePath = item.getKey().toAbsolutePath().toString();

			try (ArchiveReader ar = new ArchiveReader(absolutePath)) {
				if (ar.getImageCount() == 0) {
					continue;
				}
				
				String md5 = getFileMd5(absolutePath);
				
				System.out.println(item.getKey());
				Archive a = pathToArchive(repo, path, item.getKey(), item.getValue(), md5, ar.getImageCount());
				ArchiveThumbnail at = createThumbnail(ar.getFirstImage(), absolutePath);
				AddArchiveRequest request = new AddArchiveRequest(a, at);
				conn.enqueueTask(request, BackendTaskPriority.LOW);
			} catch (RejectedExecutionException e) {
				logger.error("Backend shut down while indexer was indexing", e);
				break;
			} catch (Exception e) {
				logger.error("Error occured while persisting archive " +
						item.getKey().toString() + " : " + e.getMessage(), e);
			}
		}
	}
	
	private HashMap<Path, Long> crawlDisk() throws IOException {
		Path[] excludes = { };
		
		FilesystemCrawler fsc = new FilesystemCrawler(path, excludes, EXTENSION_ARRAY);
		HashMap<Path, Long> paths = fsc.findFiles();
		return paths;
	}
	
	private static HashMap <Path, Archive> archivesToPathArchiveHashMap(ArchiveRepository ar, List <Archive> archives) {
		HashMap <Path, Archive> archivesMap = new HashMap<>();
		String basePath = ar.getPath();
		
		for (Archive a : archives) {
			Path fullPath = FileSystems.getDefault().getPath(basePath, a.getPath());
			archivesMap.put(fullPath, a);
		}
		return archivesMap;
	}
	
	/**
	 * Attempts to thumbnail the first image in the archive.
	 * @param firstImage compressed (in any supported format) first image, or null
	 * @param absolutePath absolute path of archive, used in error messages only
	 * @return null or compressed and scaled thumbnail in ArchiveThumbnail SQL object
	 * TODO: switch to Optional
	 */
	private static ArchiveThumbnail createThumbnail(byte[] firstImage, String absolutePath) {
		ArchiveThumbnail thumbnail = null;
		byte[] scaledImage = null;
		try {
			scaledImage = Thumbnailer.scale(firstImage);
			if (scaledImage != null) {
				thumbnail = new ArchiveThumbnail();
				thumbnail.setThumbnail(scaledImage);
			}
		} catch (IOException e) {
			logger.error("Failed to make thumbnail of archive: " + absolutePath);
		}
		return thumbnail;
	}
	
	private static Archive pathToArchive(ArchiveRepository repo, Path repoBasePath, Path filePath, Long lastModified, String md5sum, Integer pageCount) throws Exception {
		Archive archive = new Archive();
		archive.setPath(repoBasePath.relativize(filePath).toString());
		archive.setLastModified(lastModified);
		archive.setMd5Hash(md5sum);
		archive.setError(false);
		archive.setMissing(false);
		archive.setArchiveRepository(repo);
		archive.setPageCount(pageCount);
		return archive;
	}
	
	/**
	 * Returns the MD5 of a file.
	 * @throws Exception If anything goes wrong, and plenty can.
	 */
	private static String getFileMd5(String path) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream is = null;
		DigestInputStream dis = null;
		NullOutputStream nos = null;
		try {
			is = Files.newInputStream(Paths.get(path));
			dis = new DigestInputStream(is, md);
			nos = new NullOutputStream();
			IOUtils.copy(dis, nos);
		} finally {
			IOUtils.closeQuietly(dis);
			IOUtils.closeQuietly(nos);
		}
		byte[] digest = md.digest();
		return new String(Hex.encodeHex(digest));
	}	

}
