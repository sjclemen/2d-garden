package ca.twodee.eventbus.message;

import ca.twodee.schema.ArchiveRepository;

public class CreateArchiveRepositoryMessage {
	public ArchiveRepository archiveRepository;
	
	public CreateArchiveRepositoryMessage(ArchiveRepository ar) {
		this.archiveRepository = ar;
	}
}
