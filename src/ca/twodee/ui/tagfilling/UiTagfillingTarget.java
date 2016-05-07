package ca.twodee.ui.tagfilling;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import ca.twodee.ui.pieces.tags.TagListCombinedWidget;

public class UiTagfillingTarget {
	private final Composite targetComposite;
	
	public UiTagfillingTarget(Composite parent) {
		targetComposite = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		targetComposite.setLayout(layout);
				
		Button OK = new Button(targetComposite, SWT.PUSH);
		OK.setText("OK");
		OK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				OK.getShell().close();
			}
		});
	}
	
	public Composite getTargetComposite() {
		return targetComposite;
	}
	
}
