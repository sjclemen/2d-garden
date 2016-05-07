package ca.twodee.ui.viewmodel;

import ca.twodee.schema.ArchiveRepository;

public class ArchiveRepositoryVM extends ObservableModel {
	private final Integer id;
	private final String path;

	public ArchiveRepositoryVM(ArchiveRepository ar) {
		id = ar.getId();
		path = ar.getPath();
	}

	public Integer getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

}
