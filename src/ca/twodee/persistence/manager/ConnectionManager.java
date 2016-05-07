package ca.twodee.persistence.manager;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;

import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.log4j.Logger;

import com.j256.ormlite.jdbc.JdbcConnectionSource;

/**
 * Maintains database connections. Used statically.
 */
public class ConnectionManager {
	private static String CONN_STR = "jdbc:derby:2dgarden;create=true";
	private static Logger logger = Logger.getLogger(ConnectionManager.class);
	
	public static JdbcConnectionSource getConnectionSource() throws SQLException {
		@SuppressWarnings("unused")
		EmbeddedDriver driver = new EmbeddedDriver();
		return new JdbcConnectionSource(CONN_STR);
	}
	
    public static String getConnectionString() {
    	return CONN_STR;
    }
		
	public static void shutdown() {
		try {
			DriverManager.getConnection("jdbc:derby:2dgarden;shutdown=true");
		} catch (SQLNonTransientConnectionException e) {
			logger.info("SQL Database shutdown.");
		} catch (SQLException e) {
			logger.error("Failed to close database.", e);
		}
	}
}
