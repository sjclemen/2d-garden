package ca.twodee.indexer.crawler;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Sets up a {@link AllFilesVisitor}.
 */
public class FilesystemCrawler {
	private final Path basePath;
	private final Path[] excludePaths;
	private final String[] desiredExtensions;
	
	public FilesystemCrawler(Path basePath, Path[] excludePaths, String[] desiredExtensions) {
		this.basePath = basePath;
		this.excludePaths = excludePaths;
		this.desiredExtensions = desiredExtensions;
	}
	
	public HashMap<Path, Long> findFiles() throws IOException {
		HashSet <Path> excludes = new HashSet <Path>();
		for (Path excludePath: excludePaths) {
			excludes.add(excludePath);
		}
		
		HashSet <String> includeExtensions = new HashSet <String>();
		includeExtensions.addAll(Arrays.asList(desiredExtensions));
				
		AllFilesVisitor visitor = new AllFilesVisitor(excludes, includeExtensions);
		
		EnumSet<FileVisitOption> walkerOptions = EnumSet.of(FileVisitOption.FOLLOW_LINKS);
		Files.walkFileTree(basePath, walkerOptions, Integer.MAX_VALUE, visitor);
		
		return visitor.getFoundPaths();
	}
}
