package ca.twodee.persistence.message.archive;

import java.sql.SQLException;

import ca.twodee.eventbus.message.CreateIssueAndArchiveMessage;
import ca.twodee.persistence.manager.DaoCacher;
import ca.twodee.persistence.message.PersistenceRequest;
import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveThumbnail;
import ca.twodee.schema.Issue;

/**
 * Adds an archive, with an optional thumbnail, and creates an issue for it.
 */
public class AddArchiveRequest extends PersistenceRequest <Void> {
	private final Archive archive;
	private final ArchiveThumbnail thumbnail;
	public AddArchiveRequest(Archive a) {
		archive = a;
		thumbnail = null;
	}
	
	public AddArchiveRequest(Archive a, ArchiveThumbnail thumbnail) {
		archive = a;
		this.thumbnail = thumbnail;
	}
	
	@Override
	public Void handleRequest(DaoCacher daoCacher) throws SQLException {
		if (thumbnail != null) {	
			daoCacher.getArchiveThumbnailDao().create(thumbnail);
			archive.setThumbnail(thumbnail);
		}
		
		daoCacher.getArchiveDao().create(archive);
		
		Issue newIssue = new Issue();
		newIssue.setArchive(archive);
		newIssue.setName(archive.getPath());
		daoCacher.getIssueDao().create(newIssue);
		
		eventBus.post(new CreateIssueAndArchiveMessage(newIssue, archive));
		
		return null;
   	}

}
