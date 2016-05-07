package ca.twodee.persistence.message.archive;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;

import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.ArchiveThumbnail;

/**
 * Retrives a thumbnail from the database, or null if we don't have
 * a thumbnail for this particular archive.
 */
public class GetArchiveThumbnailRequest extends PersistenceRequest<ArchiveThumbnail> {
	private final Integer archiveId;
	
	public GetArchiveThumbnailRequest(Integer archiveId) {
		this.archiveId = archiveId;
	}

	@Override
	public ArchiveThumbnail handleRequest(DaoCacher daoCacher)
			throws SQLException {
		Dao<ArchiveThumbnail, Integer> atd = daoCacher.getArchiveThumbnailDao();
		ArchiveThumbnail thumbnail = atd.queryForId(archiveId);
		return thumbnail;
	}

}
