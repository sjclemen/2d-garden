package ca.twodee.indexer.archive;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.imgscalr.Scalr;

public class Thumbnailer {
	private final static Integer MAX_THUMB_W_PIXELS = 500;
	private final static Integer MAX_THUMB_H_PIXELS = 500;
	
	/**
	 * Scales down a compressed image and outputs it in compressed form.
	 * @param compressedInput null or compressed image data (png, jpg, etc.)
	 * @return null or compressed image data
	 */
	public static byte[] scale(byte[] compressedInput) throws IOException {
		if (compressedInput == null) {
			return null;
		}
		
		ByteArrayInputStream bais = new ByteArrayInputStream(compressedInput);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			BufferedImage imag = ImageIO.read(bais);
			// decoding failed? do not scale or we'll get IllegalArgumentException
			if (imag == null) {
				return null;
			}
			BufferedImage thumb = Scalr.resize(imag, Scalr.Mode.AUTOMATIC, MAX_THUMB_W_PIXELS, MAX_THUMB_H_PIXELS);
			ImageIO.write(thumb, "png", baos);
			return baos.toByteArray();
		} finally {
			IOUtils.closeQuietly(bais);
			IOUtils.closeQuietly(baos);
		}
	}

}
