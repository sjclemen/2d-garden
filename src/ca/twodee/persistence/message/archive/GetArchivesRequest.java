package ca.twodee.persistence.message.archive;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Archive;

public class GetArchivesRequest extends PersistenceRequest <List <Archive>> {
	private final Integer repositoryId;
	
	public GetArchivesRequest() {
		this.repositoryId = null;
	}
	
	public GetArchivesRequest(int repositoryId) {
		this.repositoryId = repositoryId;
	}

	@Override
	public List <Archive> handleRequest(DaoCacher daoCacher) throws SQLException {
		Dao<Archive, Integer> archiveDao = daoCacher.getArchiveDao();

		List<Archive> results;
		if (repositoryId != null) {
			results = archiveDao.queryForEq(Archive.REPOSITORY_FN, repositoryId);
		} else {
			results = archiveDao.queryForAll();
		}
		return results;

	}

}
