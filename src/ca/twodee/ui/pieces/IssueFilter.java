package ca.twodee.ui.pieces;

import java.util.ArrayList;
import java.util.HashSet;

import org.eclipse.core.databinding.observable.set.ISetChangeListener;
import org.eclipse.core.databinding.observable.set.SetChangeEvent;
import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import ca.twodee.ui.viewmodel.IssueVM;
import ca.twodee.ui.viewmodel.TagVM;

/**
 * Filters search. This works by being bound to a viewer and a set of issue tags, along with
 * having a regularly updated search term. Whenever the inputs change, {@link updateViewers} is
 * called, which causes the view to reinquire about the filtered status of each item.
 * TODO: Stop using a hash set for the selected tags? Is caching of some kind necessary?
 * TODO: Benchmark this, because it's going to be called a lot.
 */
public class IssueFilter extends ViewerFilter {
	private final ArrayList<String> searchTerms;
	private final HashSet<TagVM> tagSet = new HashSet<TagVM>();
	private Boolean operationIsAnd = true;
	private Viewer callOnChange = null;
	
	public IssueFilter() {
		this.searchTerms = new ArrayList<String>();
	}
	
	public void setViewer(Viewer callOnChange) {
		this.callOnChange = callOnChange;
	}
	
	public void setSearchText(String searchText) {
		this.searchTerms.clear();
		if (searchText.isEmpty()) {
			updateViewers();
			return;
		}
		
		String trimmed = searchText.trim().toLowerCase();
		int spacePos = trimmed.indexOf(' ');
		if (spacePos == -1) {
			searchTerms.add(trimmed);
			updateViewers();
			return;
		}
		
		String[] terms = trimmed.split("\\s+");
		for (String s : terms) {
			searchTerms.add(s);
		}

		updateViewers();
	}
	
	public void bindIssueTags(WritableSet eventTagSet) {
		eventTagSet.addSetChangeListener(new ISetChangeListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleSetChange(SetChangeEvent event) {
				tagSet.addAll(event.diff.getAdditions());
				tagSet.removeAll(event.diff.getRemovals());
				
				updateViewers();
			}
			
		});
	}
	
	public void setIssueTagOperation(boolean operationIsAnd) {
		this.operationIsAnd = operationIsAnd;
		
		updateViewers();
	}

	private void updateViewers() {
		if (callOnChange != null) {
			callOnChange.refresh();
		}
	}

	@Override
	public boolean select(Viewer tableViewer, Object parentElement, Object element) {
		IssueVM issue = (IssueVM)element;
		
		TAGCONDITION: if (!tagSet.isEmpty()) {
			if (operationIsAnd && !issue.getIssueTags().containsAll(tagSet)) {
				return false;
			} else {
				for (Object tagRaw : tagSet) {
					if (issue.getIssueTags().contains(tagRaw)) {
						break TAGCONDITION;
					}
				}
				return false;
			}
		}
		
		if (!searchTerms.isEmpty()) {
			String issueLowercase = issue.getName().toLowerCase();
			
			for (String term : searchTerms) {
				if (!issueLowercase.contains(term)) {
					return false;
				}
			}
		}
		
		return true;
	}

}
