package ca.twodee.indexer.archive;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.SevenZipException;

public class InMemorySequentialOutStream implements ISequentialOutStream {
	private final ByteArrayOutputStream baos;
	
	public InMemorySequentialOutStream() {
		baos = new ByteArrayOutputStream();
	}
	
	public byte[] getBytes() {
		return baos.toByteArray();
	}

	@Override
	public int write(byte[] arg0) throws SevenZipException {
		// TODO: impose a maximum size limit
		try {
			baos.write(arg0);
		} catch (IOException e) {
			throw new SevenZipException(e);
		}
		return arg0.length;
	}

}
