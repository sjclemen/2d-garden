package ca.twodee.ui.pieces;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ca.twodee.ui.pieces.tags.TagListCombinedWidget;

public class SearchArea {
	private final Composite searchArea;
	private final Text searchText;
	private final TagListCombinedWidget tagList;
	private final Button tagAny;
	private final Button tagAll;
	
	public SearchArea(Composite parent) {
		searchArea = new Composite(parent, SWT.NONE);
		searchArea.setLayout(new FormLayout());
		final GridData gridDataSearchArea = new GridData();
		gridDataSearchArea.grabExcessHorizontalSpace = true;
		gridDataSearchArea.horizontalAlignment = GridData.FILL;
		searchArea.setLayoutData(gridDataSearchArea);

		// TBD: need constant width for labels of the size of the largest text
		final Label l1 = new Label(searchArea, SWT.RIGHT);
		l1.setText("Search");
		Point l1DesiredSize = l1.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		FormData fd = new FormData();
		fd.width = l1DesiredSize.x + 10;
		fd.top = new FormAttachment(0, 10);
		fd.left = new FormAttachment(0, 0);
		l1.setLayoutData(fd);

		final Label l2 = new Label(searchArea, SWT.RIGHT);
		l2.setText("Tags");
		fd = new FormData();
		fd.width = l1DesiredSize.x + 10;
		fd.top = new FormAttachment(l1, 20);
		fd.left = new FormAttachment(0, 0);
		l2.setLayoutData(fd);

		searchText = new Text(searchArea, SWT.BORDER | SWT.SINGLE);
		fd = new FormData();
		fd.width = 200;
		fd.top = new FormAttachment(l1, 0, SWT.TOP);
		fd.left = new FormAttachment(l1, 10);
		searchText.setLayoutData(fd);

		tagList = new TagListCombinedWidget(searchArea, SWT.NONE, parent, false);
		fd = new FormData();
		fd.width = 200;
		fd.top = new FormAttachment(l2, 0, SWT.TOP);
		fd.left = new FormAttachment(l2, 10);
		tagList.setLayoutData(fd);
		
		tagAny = new Button(searchArea, SWT.RADIO);
		tagAny.setText("Any");
		fd = new FormData();
		fd.top = new FormAttachment(tagList, 0, SWT.TOP);
		fd.left = new FormAttachment(tagList, 10);
		tagAny.setLayoutData(fd);
		
		tagAll = new Button(searchArea, SWT.RADIO);
		tagAll.setText("All");
		fd = new FormData();
		fd.top = new FormAttachment(tagAny, 0, SWT.TOP);
		fd.left = new FormAttachment(tagAny, 10);
		tagAll.setLayoutData(fd);
		
		searchArea.layout();
	}
	
	public Text getSearchText() {
		return searchText;
	}
	
	public TagListCombinedWidget getTagList() {
		return tagList;
	}
	
	public Button getTagAnyRadio() {
		return tagAny;
	}
	
	public Button getTagAllRadio() {
		return tagAll;
	}

}
