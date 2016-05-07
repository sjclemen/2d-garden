package ca.twodee.ui.pieces.tags;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import ca.twodee.ui.pieces.tags.TagListSelected.TagToolItemConfigurator;
import ca.twodee.ui.viewmodel.TagCategoryVM;
import ca.twodee.ui.viewmodel.TagVM;

public class TagListSelectedGroup extends Composite implements TagListSelectedInterface {
	private final static Logger logger = Logger.getLogger(TagListSelectedGroup.class);
	
	// Selected tags in the writeable set which is watchable.
	private final WritableSet selectedTags = new WritableSet(new ArrayList<TagVM>(), TagVM.class);
	// Selected TagCategories, in a writeable set which is watchable.
	private final HashSet<TagCategoryVM> selectedTagCategories = new HashSet<>();
	// Selected tag categories, in a string -> TagListSelectedWidgetGroup pair.
	private final TreeMap<String, TagListSelectedWidgetGroup> tagCategoryTree = new TreeMap<>();

	private final Runnable callLayoutOnMe;

	public TagListSelectedGroup(Composite parent, int style, Runnable callLayout) {
		super(parent, style);
		this.callLayoutOnMe = callLayout;
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		setLayout(layout);
		
		selectedTags.addSetChangeListener(new ISetChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleSetChange(SetChangeEvent arg0) {
				Set<TagVM> tagVmAdditions = arg0.diff.getAdditions();
				for (TagVM tvm: tagVmAdditions) {
					addTagToSelected(tvm);
				}
				
				Set<TagVM> tagVmDeletions = arg0.diff.getRemovals();
				for (TagVM tvm: tagVmDeletions) {
					removeTagFromSelected(tvm);
				}

				callLayoutOnMe.run();
			}
		});
	}
	
	public TagListSelectedGroup(Composite parent, int style, Composite callLayout) {
		this(parent, style, new Runnable() {
			public void run() {
				callLayout.layout(true, true);
			}
		});
	}
	
	public WritableSet getSelectedTags() {
		return selectedTags;
	}	

	@Override
	public Composite getWidget() {
		return this;
	}
	
	private void addTagToSelected(TagVM tagVM) {
		TagCategoryVM tagCategoryVM = tagVM.getTagCategory();
		if (selectedTagCategories.contains(tagCategoryVM)) {
			TagListSelectedWidgetGroup widgetGroup = tagCategoryTree.get(tagCategoryVM.getName());
			widgetGroup.selected.getSelectedTags().add(tagVM);
		} else {
			// TODO: instead of names, use a priority ID
			Label label = new Label(this, SWT.NONE);
			label.setText(tagCategoryVM.getName());
			// we're going to ask it to layout this widget
			TagListSelected selected = new TagListSelected(this, SWT.NONE, new Runnable() {

				@Override
				public void run() {
					// DO NOTHING AT ALL, the layout run occurs in the handleChangeSet
					// called from the constructor.
				}
				
			});
			TagListSelectedWidgetGroup widgetGroup = new TagListSelectedWidgetGroup(label, selected);
			tagCategoryTree.put(tagCategoryVM.getName(), widgetGroup);
			
			Entry <String, TagListSelectedWidgetGroup> previousGroup = tagCategoryTree.lowerEntry(tagCategoryVM.getName());
			if (previousGroup == null) {
				Entry <String, TagListSelectedWidgetGroup> followingGroup = tagCategoryTree.higherEntry(tagCategoryVM.getName());
				if (followingGroup == null) {
					// do nothing, there's nobody else here!
				} else {
					// otherwise move above following TagCategory
					label.moveAbove(followingGroup.getValue().tagCategoryName);
					selected.moveBelow(label);
				}
			} else {
				// and this is the easy case where we can place it right after its predecessor
				label.moveBelow(previousGroup.getValue().selected);
				selected.moveBelow(label);
			}
			selected.setItemActionConfigurator(new TagToolItemConfigurator() {

				@Override
				public void setupToolbar(ToolBar toolBar, TagVM tag) {
					ToolItem item = new ToolItem(toolBar, SWT.PUSH);
					item.setText("X");
					item.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetDefaultSelected(SelectionEvent arg0) {
							selectedTags.remove(tag);
						}

						@Override
						public void widgetSelected(SelectionEvent arg0) {
							selectedTags.remove(tag);
						}
						
					});

				}
				
			});
			selected.getSelectedTags().add(tagVM);
			selectedTagCategories.add(tagCategoryVM);
		}
	}
	
	private void removeTagFromSelected(TagVM tagVM) {
		TagCategoryVM tagCategoryVM = tagVM.getTagCategory();
		if (selectedTagCategories.contains(tagCategoryVM)) {
			TagListSelectedWidgetGroup widgetGroup = tagCategoryTree.get(tagCategoryVM.getName());
			widgetGroup.selected.getSelectedTags().remove(tagVM);
			if (widgetGroup.selected.getSelectedTags().size() == 0) {
				widgetGroup.selected.dispose();
				widgetGroup.tagCategoryName.dispose();
				selectedTagCategories.remove(tagCategoryVM);
				tagCategoryTree.remove(tagCategoryVM.getName());
			}
		} else {
			logger.info("Received request to remove " + tagVM.toString() + " but TagCategory does not exist.");
		}
	}
	
	private class TagListSelectedWidgetGroup {
		public final Label tagCategoryName;
		public final TagListSelected selected;
		
		public TagListSelectedWidgetGroup(Label tagCategoryName, TagListSelected selected) {
			this.tagCategoryName = tagCategoryName;
			this.selected = selected;
		}
	}

}
