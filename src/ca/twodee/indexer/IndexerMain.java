package ca.twodee.indexer;

import ca.twodee.backend.manager.BackendConnection;

/**
 * Entry point for indexing. Maintains the connection to the backend and starts the actual
 * indexer.
 * TODO: read events from connection, rewrite indexer.
 */
public class IndexerMain implements Runnable {
	private final static String INDEX_DIRS_DEFAULT = "INSERT PATH";
	private final BackendConnection backendConnection;
	private final String[] indexDirectories;
	
	public IndexerMain(BackendConnection connection) {
		this(connection, new String[] { INDEX_DIRS_DEFAULT });
	}
	
	public IndexerMain(BackendConnection connection, String[] indexDirectories) {
		this.backendConnection = connection;
		this.indexDirectories = indexDirectories;
	}

	@Override
	public void run() {	
		for (int i = 0; i < indexDirectories.length; i++) {
			Indexer indexer = new Indexer(backendConnection, indexDirectories[i]);
			indexer.indexPath();
		}
	}

}
