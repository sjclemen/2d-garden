package ca.twodee.ui.pieces;


import org.eclipse.swt.widgets.Shell;

import com.google.common.base.Optional;
import com.google.common.util.concurrent.ListenableFuture;

import ca.twodee.ui.viewmodel.IssueListVM;
import ca.twodee.ui.viewmodel.TagCategoryVM;
import ca.twodee.ui.viewmodel.TagVM;

/**
 * Handles the creation of a {@link CreateTagDialog} and then calls the viewmodel for the
 * creation of an a tag, returning a future for it.
 */
public class CreateTagAction {
	private final IssueListVM viewModel;
	private final CreateTagDialog dialog;
	
	public CreateTagAction(Shell parentWindow, IssueListVM viewModel, String suggestion) {
		this.viewModel = viewModel;
		dialog = new CreateTagDialog(parentWindow, suggestion, viewModel.getTagCategoryViewModel());
	}
	
	public Optional<ListenableFuture<TagVM>> getNewTag() {
		dialog.open();
		String newTagName = dialog.getNewTagName();
		Optional<TagCategoryVM> tagCategory = dialog.getTagCategory();
		if (tagCategory.isPresent()) {
			ListenableFuture<TagVM> tagFuture =
					viewModel.getTagViewModel().addTag(newTagName, tagCategory.get());
			
			return Optional.of(tagFuture);
		}
		return Optional.absent();
	}
}
