package ca.twodee.ui.helpers;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import ca.twodee.ui.viewmodel.IssueVM;

/**
 * Allows issues to be compared for use in data binding. Sorts on the various properties of
 * an issue.
 * TODO: implement equals.
 */
public class IssueViewerComparator extends ViewerComparator {
	private Direction direction;
	private CompareTarget target;
	
	public IssueViewerComparator() {
		this.direction = Direction.ASCENDING;
		this.target = CompareTarget.NAME;
	}
	
	public void setTarget(CompareTarget target) {
		if (this.target.equals(target)) {
			if (this.direction == Direction.ASCENDING) {
				this.direction = Direction.DESCENDING;
			} else {
				this.direction = Direction.ASCENDING;
			}
			return;
		}
		
		this.target = target;
		
		if (target.equals(CompareTarget.RATING) || target.equals(CompareTarget.TAGS)) {
			this.direction = Direction.DESCENDING;
		} else {
			this.direction = Direction.ASCENDING;
		}
	}
	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		IssueVM issue1 = (IssueVM)e1;
		IssueVM issue2 = (IssueVM)e2;
		
		int result = compareItems(issue1, issue2, target);
		
		if (direction == Direction.DESCENDING) {
			return 0 - result;
		}
		return result;
	}
	
	private static int compareItems(IssueVM issue1, IssueVM issue2, CompareTarget target) {
		switch (target) {
			case NAME:
				return issue1.getName().compareTo(issue2.getName());
			case TAGS:
				return Integer.compare(issue1.getIssueTags().size(), issue2.getIssueTags().size());
			case RATING:
				return issue1.getRating().compareTo(issue2.getRating());
			default:
				return 0;
		}
	}
	
	public enum Direction {
		ASCENDING,
		DESCENDING
	}
	
	public enum CompareTarget {
		NAME,
		TAGS,
		RATING
	}
	
	public static class ColumnSelectionListener implements SelectionListener {
		private final TableViewer viewer;
		private final CompareTarget target;
		
		public ColumnSelectionListener(TableViewer viewer, CompareTarget target) {
			this.viewer = viewer;
			this.target = target;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			IssueViewerComparator comparator = (IssueViewerComparator) viewer.getComparator();
			comparator.setTarget(target);
			viewer.refresh();
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			IssueViewerComparator comparator = (IssueViewerComparator) viewer.getComparator();
			comparator.setTarget(target);
			viewer.refresh();
		}
		
	}
}
