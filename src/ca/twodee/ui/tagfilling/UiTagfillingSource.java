package ca.twodee.ui.tagfilling;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public abstract class UiTagfillingSource {
	protected final Group mainContainer;
	
	public UiTagfillingSource(Composite parent) {		
		mainContainer = new Group(parent, SWT.NONE);
		mainContainer.setLayout(new RowLayout());
		mainContainer.setText("Your mom");
	}
	
	public Composite getMainContainer() {
		return mainContainer;
	}
	
}
