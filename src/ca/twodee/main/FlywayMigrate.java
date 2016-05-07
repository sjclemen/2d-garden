package ca.twodee.main;

import org.flywaydb.core.Flyway;

import ca.twodee.persistence.manager.ConnectionManager;

public class FlywayMigrate {

	public static void main(String[] args) {
		Flyway flyway = new Flyway();
		flyway.setDataSource(ConnectionManager.getConnectionString(), null, null);
		flyway.migrate();
	}

}
