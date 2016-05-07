package ca.twodee.ui.pieces.tags;

import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import ca.twodee.ui.pieces.CreateTagAction;
import ca.twodee.ui.pieces.tags.TagListCombo.TagListSelectedEventHandler;
import ca.twodee.ui.viewmodel.IssueListVM;
import ca.twodee.ui.viewmodel.TagListVMEventListener;
import ca.twodee.ui.viewmodel.TagVM;

public class TagListVMBinding {
	// Issue view model.
	private final IssueListVM issueList;
	private final TagListCombo tagList;
	private final TagListSelectedInterface tagListSelected;
	
	public TagListVMBinding(IssueListVM issueList, TagListCombo tagList, TagListSelectedInterface tagListSelected) {
		this.issueList = issueList;
		this.tagList = tagList;
		this.tagListSelected = tagListSelected;
		bindToTagList();
	}
	
	public TagListVMBinding(IssueListVM viewModel, TagListCombinedWidget combined) {
		this(viewModel, combined.getTagListCombo(), combined.getTagListSelected());
	}

	private void bindToTagList() {
		tagList.addEventHandler(new TagListSelectedEventHandler() {

			@Override
			public void addTagToSet(String name) {
				TagVM newTag = issueList.getTagViewModel().getTagVMByName(name);
				if (newTag != null) {
					tagListSelected.getSelectedTags().add(newTag);
				}
			}

			@Override
			public void requestNewTag(String name) {
				createNewTag(name);
			}
			
		});
			
		TagListVMEventListener tvmEventListener = new TagListVMEventListener() {

			@Override
			public void onRename(TagVM tag, String oldName, String newName) {
				if (tagList.getControl().isDisposed()) {
					return;
				}
				tagList.renameTagInDropdown(oldName, newName);
			}

			@Override
			public void onAdd(TagVM tag) {
				if (tagList.getControl().isDisposed()) {
					return;
				}
				tagList.addTagToDropdown(tag.getName());
			}

			@Override
			public void onRemove(TagVM tag) {
				if (tagList.getControl().isDisposed()) {
					return;
				}
				tagList.removeTagFromDropdown(tag.getName());
			}
			
		};
		issueList.getTagViewModel().addListener(tvmEventListener);
		tagList.getControl().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				issueList.getTagViewModel().removeListener(tvmEventListener);
			}
			
		});
		
		tagList.bindToViewModel(issueList.getTagViewModel().getContentProposals(),
				issueList.getTagViewModel().getNamesSorted());
	}
	
	private void createNewTag(String name) {
		CreateTagAction createTagAction = new CreateTagAction(tagListSelected.getShell(), issueList, name);
		Optional<ListenableFuture<TagVM>> newTag = createTagAction.getNewTag();
		
		if (newTag.isPresent()) {
			ListenableFuture<TagVM> tagVmFuture = newTag.get();
			// this *should* run on our thread...
			// TODO: this could come after we've already switched our view...
			// TODO: we should really fix this because it causes concurrency problems
			// TODO: the most logical way to deal with this would be to prevent switching view,
			// since if you add it to the main viewmodel, it breaks the desync
			Futures.addCallback(tagVmFuture, new FutureCallback<TagVM>() {

				@Override
				public void onSuccess(TagVM result) {
					tagListSelected.getSelectedTags().add(result);
				}

				@Override
				public void onFailure(Throwable t) {
					// it got logged somewhere else
				}
				
			});
		}
	}

}
