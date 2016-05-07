package ca.twodee.ui.viewmodel;

import java.io.ByteArrayInputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.persistence.message.archive.GetArchiveThumbnailRequest;
import ca.twodee.schema.ArchiveThumbnail;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

/**
 * The archive thumbnail cache keeps two caches:
 * - an undecoded cache of JPG/PNG files in their undecoded form
 * - a decoded cache of images which have been decoded
 * TODO: what happens when we get ridiculously large items? Do they get rejected immediately?
 * TODO: evaluate the effectiveness of this cache and consider eliminating it in favour
 * of ORM/database caches.
 */
public class ArchiveThumbnailCache {
	private final static Long MAX_CACHE_SIZE = 41943040L; // 40 MB

	private final LoadingCache<Integer, ArchiveThumbnail> undecodedCache;
	private final LoadingCache<Integer, ImageData> decodedCache;
	
	private final ImageData defaultImage = new ImageData(1, 1, 16, new PaletteData(0, 0, 0));
	
	public ArchiveThumbnailCache(BackendConnection conn) {
		this.undecodedCache = CacheBuilder
				.newBuilder()
				.concurrencyLevel(1)
				.expireAfterAccess(10, TimeUnit.MINUTES)
				.maximumWeight(MAX_CACHE_SIZE / 2)
				.weigher(new Weigher<Integer, ArchiveThumbnail>() {

					@Override
					public int weigh(Integer arg0, ArchiveThumbnail arg1) {
						return arg1.getThumbnail().length;
					}
					
				})
				.build(
						new CacheLoader<Integer, ArchiveThumbnail>() {
							@Override
							public ArchiveThumbnail load(Integer arg0) throws Exception {
								GetArchiveThumbnailRequest gatr = new GetArchiveThumbnailRequest(arg0);
								return conn.enqueueTask(gatr).get();
							}
							
						}
				);
		
		this.decodedCache = CacheBuilder
				.newBuilder()
				.concurrencyLevel(1)
				.expireAfterAccess(2, TimeUnit.MINUTES)
				.maximumWeight(MAX_CACHE_SIZE / 2)
				.weigher(new Weigher<Integer, ImageData>() {

					@Override
					public int weigh(Integer arg0, ImageData arg1) {
						return arg1.data.length;
					}
					
				})
				.build(new CacheLoader<Integer, ImageData>() {

					@Override
					public ImageData load(Integer arg0) throws Exception {
						byte[] thumbnailUndecoded = undecodedCache.get(arg0).getThumbnail();
						ByteArrayInputStream bais = new ByteArrayInputStream(thumbnailUndecoded);
						return new ImageData(bais);
					}
					
				});
	}
	
	public ImageData getImageData(Integer id) throws ExecutionException {
		return decodedCache.get(id);
	}

	public ImageData getDefaultImageData() {
		return defaultImage;
	}
}
