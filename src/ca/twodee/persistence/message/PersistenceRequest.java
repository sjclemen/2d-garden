package ca.twodee.persistence.message;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.common.eventbus.AsyncEventBus;
import com.j256.ormlite.support.ConnectionSource;

import ca.twodee.backend.manager.BackendTask;
import ca.twodee.persistence.manager.ConnectionManager;
import ca.twodee.persistence.manager.DaoCacher;

/**
 * A persistence request is any request to the backend. A thread local DAO cacher is supplied
 * to each request.
 *
 * @param <T> The return value for the task.
 */
abstract public class PersistenceRequest <T> extends BackendTask<T> {
	private static final Logger logger = Logger.getLogger(PersistenceRequest.class);
	private static final ThreadLocal<DaoCacher> daoCache = new ThreadLocal<DaoCacher>() {
		@Override
		protected DaoCacher initialValue() {
			try {
				ConnectionSource cs = ConnectionManager.getConnectionSource();
				return new DaoCacher(cs);
			} catch (SQLException e) {
				logger.error("Unable to start DaoCacher", e);
				throw new RuntimeException("Unable to start DaoCacher", e);
			}
		}
	};
	protected AsyncEventBus eventBus;
	
	public void setEventBus(AsyncEventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	public T call() throws Exception {
		DaoCacher daoCacher = daoCache.get();
		return handleRequest(daoCacher);
	}
	
	abstract public T handleRequest(DaoCacher daoCacher) throws SQLException; 
}
