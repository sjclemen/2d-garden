package ca.twodee.ui.pieces;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import ca.twodee.ui.pieces.tags.TagListCombinedWidget;

public class IssueOverview {
	private final ScrolledComposite rightSideScrollable;
	private final Composite rightSide;
	private final GridLayout rightSideLayout;
	
	private final Label imageLabel;
	private final Text nameText;
	private final TagListCombinedWidget tagsList;
	private final Spinner starsSpinner;
	private final Button fillButton;
	
	public IssueOverview(Composite parent) {
		rightSideScrollable = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.BORDER);
		rightSideScrollable.setExpandHorizontal(true);
		rightSideScrollable.setExpandVertical(true);
		rightSideScrollable.setLayout(new GridLayout());
		
		rightSide = new Composite(rightSideScrollable, SWT.NONE);
		rightSideScrollable.setContent(rightSide);
	
		rightSideLayout = new GridLayout();
		rightSideLayout.numColumns = 1;
		rightSide.setLayout(rightSideLayout);
		
		GridData gridDataRightSide = new GridData();
		gridDataRightSide.grabExcessVerticalSpace = true;
		gridDataRightSide.verticalAlignment = GridData.FILL;
		rightSide.setLayoutData(gridDataRightSide);
		
		GridData horizontalFill = new GridData();
		horizontalFill.grabExcessHorizontalSpace = true;
		horizontalFill.horizontalAlignment = GridData.FILL;
		horizontalFill.widthHint = 169; // setting any width hint at all prevents it from exceeding the fixed 375 size
		GridDataFactory hff = GridDataFactory.createFrom(horizontalFill);
		
		imageLabel = new Label(rightSide, SWT.NONE);
		hff.applyTo(imageLabel);

		final Label nameLabel = new Label(rightSide, SWT.NONE);
		nameLabel.setText("Name");
		nameText = new Text(rightSide, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL);
		hff.applyTo(nameText);
		
		final Label tagsLabel = new Label(rightSide, SWT.NONE);
		tagsLabel.setText("Tags");
		tagsList = new TagListCombinedWidget(rightSide, SWT.NONE, new Runnable() {

			@Override
			public void run() {
				rightSide.layout(true, true);
				rightSideScrollable.setMinSize(rightSide.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
			
		}, true);
		hff.applyTo(tagsList);
		
		final Label starsLabel = new Label(rightSide, SWT.NONE);
		starsLabel.setText("Rating");
		starsSpinner = new Spinner(rightSide, SWT.BORDER);
		starsSpinner.setMinimum(0);
		starsSpinner.setMaximum(10);
		
		fillButton = new Button(rightSide, SWT.PUSH);
		fillButton.setText("Fill Tags");
		
		rightSideScrollable.setMinSize(rightSide.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		rightSide.addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event arg0) {
				rightSideScrollable.setMinSize(rightSide.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
			
		});
	}
	
	public ScrolledComposite getScrolledComposite() {
		return rightSideScrollable;
	}
	
	public Composite getComposite() {
		return rightSide;
	}

	public Label getImageLabel() {
		return imageLabel;
	}

	public Text getNameText() {
		return nameText;
	}

	public TagListCombinedWidget getTagsText() {
		return tagsList;
	}

	public Spinner getStarsSpinner() {
		return starsSpinner;
	}
	
	public Button getFillTagsButton() {
		return fillButton;
	}

}
