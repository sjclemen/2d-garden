package ca.twodee.ui.viewmodel;

import java.util.List;

import org.eclipse.core.databinding.observable.map.WritableMap;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

import ca.twodee.eventbus.message.CreateArchiveRepositoryMessage;
import ca.twodee.schema.ArchiveRepository;

public class ArchiveRepositoryListVM {
	private final WritableMap archiveRepositories = new WritableMap();
	
	public ArchiveRepositoryListVM(List <ArchiveRepository> archiveRepositoryModels, AsyncEventBus eventBus) {
		for (ArchiveRepository ar : archiveRepositoryModels) {
			addArchiveRepository(ar);
		}
		
		eventBus.register(new ArchiveRepositoryEventHandler());
	}
	
	public ArchiveRepositoryVM getById(Integer id) {
		return (ArchiveRepositoryVM) archiveRepositories.get(id);
	}
	
	protected void addArchiveRepository(ArchiveRepository ar) {
		archiveRepositories.put(ar.getId(), new ArchiveRepositoryVM(ar));
	}
	
	private class ArchiveRepositoryEventHandler {
		@Subscribe
		public void createRepository(CreateArchiveRepositoryMessage msg) {
			addArchiveRepository(msg.archiveRepository);
		}
	}
}
