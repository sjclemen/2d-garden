package ca.twodee.ui.tagfilling;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import ca.twodee.ui.pieces.IssueOverview;

public class UiTagfillingMainArea {
	private final Composite boundingBox;
	private final IssueOverview issueOverview;
	private final UiTagfillingSources sources;
	private final UiTagfillingTarget target;
	
	public UiTagfillingMainArea(Composite parent) {
		boundingBox = new Composite(parent, SWT.NONE);
		GridLayout boundingBoxLayout = new GridLayout();
		boundingBoxLayout.numColumns = 2;
		boundingBox.setLayout(boundingBoxLayout);
		
		issueOverview = new IssueOverview(boundingBox);
		GridData leftSideGridData = new GridData();
		leftSideGridData.grabExcessHorizontalSpace = false;
		leftSideGridData.grabExcessVerticalSpace = true;
		leftSideGridData.verticalAlignment = GridData.FILL;
		leftSideGridData.widthHint = 375;
		issueOverview.getScrolledComposite().setLayoutData(leftSideGridData);
		
		sources = new UiTagfillingSources(boundingBox);
		GridData tagSourcesGridData = new GridData();
		tagSourcesGridData.grabExcessHorizontalSpace = true;
		tagSourcesGridData.grabExcessVerticalSpace = true;
		tagSourcesGridData.verticalAlignment = GridData.FILL;
		tagSourcesGridData.horizontalAlignment = GridData.FILL;
		sources.getTagSourcesComposite().setLayoutData(tagSourcesGridData);
		
		target = new UiTagfillingTarget(boundingBox);
		GridData tagSelectedGridData = new GridData();
		tagSelectedGridData.grabExcessHorizontalSpace = true;
		tagSelectedGridData.grabExcessVerticalSpace = false;
		tagSelectedGridData.horizontalSpan = 2;
		target.getTargetComposite().setLayoutData(tagSelectedGridData);
	}

	public IssueOverview getIssueOverview() {
		return issueOverview;
	}

	public UiTagfillingSources getSources() {
		return sources;
	}

	public UiTagfillingTarget getTarget() {
		return target;
	}

	public Composite getBoundingBox() {
		return boundingBox;
	}
}
