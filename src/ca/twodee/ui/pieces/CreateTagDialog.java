package ca.twodee.ui.pieces;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.google.common.base.Optional;

import ca.twodee.ui.viewmodel.TagCategoryListVM;
import ca.twodee.ui.viewmodel.TagCategoryVM;

/**
 * Dialog box prompting users to create a new tag.
 * TODO: show meaningful error messages for when people create dupe tags and the like.
 * TODO: prevent users from hitting OK when form not complete by switching to wizard
 */
public class CreateTagDialog extends Dialog {
	private final String suggestion;
	private final TagCategoryListVM tagCategoryViewModel;
	private Text tagInput;
	private TagCategoryListCombo tagCategoryInput;

	private String newTagName;
	private Optional<TagCategoryVM> tagCategory = Optional.absent();

	/**
	 * Creates a tag dialog, but does not open it. Call {@link open} for that.
	 * @param parentShell Shell to create this as a child of.
	 * @param suggestion A suggestion for the tag, not the tag category.
	 * @param tagCategoryViewModel Tag category view model, to bind the tag category list to.
	 */
	CreateTagDialog(Shell parentShell, String suggestion, TagCategoryListVM tagCategoryViewModel) {
		super(parentShell);
		this.suggestion = suggestion;
		this.tagCategoryViewModel = tagCategoryViewModel;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Create new tag");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		Label tagNameLabel = new Label(container, SWT.NONE);
		tagNameLabel.setText("Tag name");
		
		tagInput = new Text(container, SWT.SINGLE | SWT.BORDER);
		tagInput.setText(suggestion);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tagInput.setLayoutData(gridData);
		
		Label tagCategoryLabel = new Label(container, SWT.NONE);
		tagCategoryLabel.setText("Tag category");

		tagCategoryInput = new TagCategoryListCombo(container, SWT.NONE);
		tagCategoryInput.bindToViewModel(tagCategoryViewModel);
		tagCategoryInput.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		return container;
	}

	@Override
	protected void okPressed() {
		newTagName = tagInput.getText();
		tagCategory = tagCategoryInput.getSelectedItem();
		super.okPressed();
	}

	public String getNewTagName() {
		return newTagName;
	}

	public Optional<TagCategoryVM> getTagCategory() {
		return tagCategory;
	}

}
