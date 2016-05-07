package ca.twodee.ui.viewmodel;

import java.util.List;

import org.eclipse.core.databinding.observable.map.WritableMap;

import com.google.common.eventbus.AsyncEventBus;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveRepository;

public class ArchiveListVM {
	private final WritableMap archives;
	private final ArchiveThumbnailCache cache;
	private final ArchiveRepositoryListVM archiveRepositoryViewModel;
	private final BackendConnection conn;
	
	public ArchiveListVM(List <Archive> archiveList, List <ArchiveRepository> archiveRepositories, BackendConnection conn, AsyncEventBus eventBus) {
		archives = new WritableMap();
		cache = new ArchiveThumbnailCache(conn);
		archiveRepositoryViewModel = new ArchiveRepositoryListVM(archiveRepositories, eventBus);
		this.conn = conn;
		
		for (Archive a : archiveList) {
			addArchive(a);
		}
	}
	
	// ONLY FOR USE FOR ALREADY-CREATED ARCHIVES
	public void addArchive(Archive a) {
		archives.put(a.getId(), new ArchiveVM(a, conn, cache, archiveRepositoryViewModel));
	}
	
	public ArchiveVM getArchiveById(Integer id) {
		return (ArchiveVM)archives.get(id);
	}
}
