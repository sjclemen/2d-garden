package ca.twodee.indexer.crawler;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 * A {@link FileVisitor} which looks for all items with a certain set of extensions except those
 * starting with a particular path. If a path is a directory, all trees under will not be visited.
 */
public class AllFilesVisitor implements FileVisitor<Path> {
	private final static Logger logger = Logger.getLogger(AllFilesVisitor.class);
	private final HashSet <Path> excludePaths;
	private final HashSet <String> includeExtensions;
	private final HashMap <Path, Long> foundPaths = new HashMap <>();
	
	/**
	 * Creates an AllFilesVisitor.
	 * @param excludePaths Paths to not consider. 
	 * @param includeExtensions All extensions to pull in.
	 */
	public AllFilesVisitor(HashSet <Path> excludePaths, HashSet <String> includeExtensions) {
		this.excludePaths = excludePaths;
		this.includeExtensions = includeExtensions;
	}
	
	public HashMap <Path, Long> getFoundPaths() {
		return foundPaths;
	}

	/**
	 * Do not visit subtrees which are on the exclude list.
	 */
	@Override
	public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
			throws IOException {
		if (excludePaths.contains(dir)) {
			return FileVisitResult.SKIP_SUBTREE;
		}
		return FileVisitResult.CONTINUE;
	}

	/**
	 * Do not consider files on the exclude list.
	 */
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
			throws IOException {
		String extension = FilenameUtils.getExtension(file.toString()).toLowerCase();
		if (excludePaths.contains(file) || !includeExtensions.contains(extension)) {
			return FileVisitResult.CONTINUE;
		}
		if (attrs.isRegularFile()) {
			foundPaths.put(file, attrs.lastModifiedTime().toMillis());
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc)
			throws IOException {
		logger.info("Failed to visit file in visitor: " + file.toString(), exc);
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc)
			throws IOException {
		return FileVisitResult.CONTINUE;
	}

}
