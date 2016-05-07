package ca.twodee.ui.tagfilling;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ca.twodee.ui.pieces.tags.TagListSelectedGroup;

public class UiTagfillingSourceOptions extends UiTagfillingSource {
	private final CloneableTextWithLabel name;
	private final CloneableTextWithLabel secondaryName;
	private final TagListSelectedGroup tagsAvailable;
	private final Composite attributesContainer;
	
	public UiTagfillingSourceOptions(Composite parent) {
		super(parent);
		mainContainer.setLayout(new RowLayout());
		
		Button b = new Button(mainContainer, SWT.PUSH | SWT.BORDER);
		b.setText("will be thumbnail");
		
		attributesContainer = new Composite(mainContainer, SWT.NONE);
		GridLayout attributesContainerLayout = new GridLayout();
		attributesContainerLayout.numColumns = 2;
		attributesContainer.setLayout(attributesContainerLayout);
		
		name = new CloneableTextWithLabel(attributesContainer, "Name");
		secondaryName = new CloneableTextWithLabel(attributesContainer, "Secondary Name");
		// TODO: cause relayout on parent
		tagsAvailable = new TagListSelectedGroup(attributesContainer, SWT.NONE, parent);
		GridData tagsAvailableGridData = new GridData();
		
		Label separator = new Label(attributesContainer, SWT.HORIZONTAL | SWT.SEPARATOR);
		GridData separatorGridData = new GridData(GridData.FILL_HORIZONTAL);
		separatorGridData.horizontalSpan = 2;
	    separator.setLayoutData(separatorGridData);
		
		tagsAvailableGridData.horizontalSpan = 2;
		tagsAvailable.setLayoutData(tagsAvailableGridData);
		
		Label belowSeperatorTest = new Label(attributesContainer, SWT.NONE);
		belowSeperatorTest.setText("woop");
		
	}
	
	private static class CloneableTextWithLabel {
		public final Label label;
		public final Composite textAndCopyContainer;
		public final Text text;
		public final Button copy;
		
		public CloneableTextWithLabel(Composite parent, String labelName) {
			label = new Label(parent, SWT.NONE);
			label.setText(labelName);
			
			textAndCopyContainer = new Composite(parent, SWT.NONE);
			textAndCopyContainer.setLayout(new RowLayout());
			
			text = new Text(textAndCopyContainer, SWT.SINGLE);
			text.setText("name goes here");
			copy = new Button(textAndCopyContainer, SWT.PUSH);
			copy.setText("COPY");
		}
	}
	
}
