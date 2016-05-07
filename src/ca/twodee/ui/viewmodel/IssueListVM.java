package ca.twodee.ui.viewmodel;

import java.util.List;

import org.eclipse.core.databinding.observable.list.WritableList;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.Subscribe;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.eventbus.message.CreateIssueAndArchiveMessage;
import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveRepository;
import ca.twodee.schema.Issue;
import ca.twodee.schema.Tag;
import ca.twodee.schema.TagCategory;

public class IssueListVM {
	protected final WritableList issueWritable;
	protected final TagListVM tagViewModel;
	protected final TagCategoryListVM tagCategoryViewModel;
	protected final ArchiveListVM archiveViewModel;
	protected final BackendConnection conn;
	
	public IssueListVM(List <Issue> issues, List <Tag> tags, List <TagCategory> tagCategories, List<Archive> archives, List <ArchiveRepository> archiveRepositories, BackendConnection conn, AsyncEventBus eventBus) {
		archiveViewModel = new ArchiveListVM(archives, archiveRepositories, conn, eventBus);
		tagCategoryViewModel = new TagCategoryListVM(tagCategories, conn);
		tagViewModel = new TagListVM(tags, tagCategoryViewModel, conn);
		this.conn = conn;
		
		issueWritable = new WritableList();
		for (Issue issue : issues) {
			addIssue(issue);
		}
		
		eventBus.register(new IssueEventHandler());
	}

	public WritableList getIssueWritable() {
		return issueWritable;
	}

	public TagListVM getTagViewModel() {
		return tagViewModel;
	}
	
	public TagCategoryListVM getTagCategoryViewModel() {
		return tagCategoryViewModel;
	}
	
	protected void addIssue(Issue issue) {
		issueWritable.add(new IssueVM(issue, tagViewModel, archiveViewModel, conn));
	}
	
	public class IssueEventHandler {
		
		@Subscribe
		public void addIssueAndArchiveHandle(CreateIssueAndArchiveMessage msg) {
			archiveViewModel.addArchive(msg.archive);
			addIssue(msg.issue);
		}
	}
}
