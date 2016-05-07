package ca.twodee.persistence.message.archive;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import ca.twodee.eventbus.message.CreateArchiveRepositoryMessage;
import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.ArchiveRepository;

/**
 * Creates an archive repository if it doesn't exist, otherwise retrieves it.
 */
public class AddOrRetrieveArchiveRepositoryRequest extends PersistenceRequest <ArchiveRepository> {
	private final String path;
	
	public AddOrRetrieveArchiveRepositoryRequest(String path) {
		this.path = path;
	}

	@Override
	public ArchiveRepository handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<ArchiveRepository, Integer> archiveRepositoryDao = daoCacher.getArchiveRepositoryDao();
		
		List <ArchiveRepository> reposMatching = archiveRepositoryDao.queryForEq(ArchiveRepository.PATH_FN, path);
		
		ArchiveRepository ar;
		
        if (reposMatching.isEmpty()) {
        	ar = new ArchiveRepository();
        	ar.setPath(this.path);
        	archiveRepositoryDao.create(ar);
        	eventBus.post(new CreateArchiveRepositoryMessage(ar));
        } else {
        	ar = reposMatching.get(0);
        }
        
        return ar;
	}

}
