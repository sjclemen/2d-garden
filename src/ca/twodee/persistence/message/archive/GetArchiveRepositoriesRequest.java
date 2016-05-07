package ca.twodee.persistence.message.archive;

import java.sql.SQLException;
import java.util.List;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.ArchiveRepository;

public class GetArchiveRepositoriesRequest extends PersistenceRequest<List <ArchiveRepository>> {

	@Override
	public List <ArchiveRepository> handleRequest(DaoCacher daoCacher) throws SQLException {
		return daoCacher.getArchiveRepositoryDao().queryForAll();
	}

}
