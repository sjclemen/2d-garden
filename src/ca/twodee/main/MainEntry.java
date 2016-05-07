package ca.twodee.main;

import java.util.concurrent.TimeUnit;

import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.flywaydb.core.Flyway;

import com.google.common.eventbus.AsyncEventBus;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.backend.manager.BackendConnectionDisplay;
import ca.twodee.backend.manager.BackendManager;
import ca.twodee.indexer.IndexerMain;
import ca.twodee.persistence.manager.ConnectionManager;
import ca.twodee.ui.pieces.MainWindow;

public class MainEntry {
	private final static Logger logger = Logger.getLogger(MainEntry.class);

	public static void main(String[] args) throws InterruptedException {
		// update database
		Flyway flyway = new Flyway();
		flyway.setDataSource(ConnectionManager.getConnectionString(), null, null);
		flyway.migrate();

		// start 7zip, because it has platform-specific native code
		try {
			SevenZip.initSevenZipFromPlatformJAR();
		} catch (SevenZipNativeInitializationException e) {
			logger.error("Failed to initialize 7zip: " + e.getMessage(), e);
			System.exit(-1);
		}

		// SWT init, event bus
		Display display = new Display();
		AsyncEventBus eventBus = new AsyncEventBus("to gui",
				new BackendConnectionDisplay.DisplayExecutor(display));

		// start database manager
		BackendManager backend = new BackendManager(eventBus);

		// main UI, setup
		BackendConnection connection = backend.getDisplayConnection(display);
		MainWindow mw = new MainWindow(display, connection, eventBus);

		// indexer
		IndexerMain indexer = new IndexerMain(backend.getRegularConnection());
		Thread indexerThread = new Thread(indexer);
		indexerThread.start();

		// run UI, will block
		mw.mainLoop();

		// prevent enqueueing of new connections
		backend.requestShutdown();

		// stop database manager, close connections
		backend.waitForShutdown(60, TimeUnit.SECONDS);
		ConnectionManager.shutdown();

	}
}
