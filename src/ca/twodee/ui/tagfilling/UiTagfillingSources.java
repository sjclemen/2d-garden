package ca.twodee.ui.tagfilling;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class UiTagfillingSources {
	private final ScrolledComposite tagSources;
	private final Composite tagSourcesInternal;
	
	public UiTagfillingSources(Composite parent) {		
		tagSources = new ScrolledComposite(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tagSources.setExpandHorizontal(true);
		tagSources.setExpandVertical(true);
		tagSources.setLayout(new FillLayout());
		
		tagSourcesInternal = new Composite(tagSources, SWT.NONE);
		GridLayout scrolledLayout = new GridLayout();
		scrolledLayout.numColumns = 1;
		tagSourcesInternal.setLayout(scrolledLayout);
		
		tagSources.setContent(tagSourcesInternal);

		UiTagfillingSourceOptions options = new UiTagfillingSourceOptions(tagSourcesInternal);
		options.getMainContainer().setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		UiTagfillingSourceOptions options2 = new UiTagfillingSourceOptions(tagSourcesInternal);
		UiTagfillingSourceOptions options3 = new UiTagfillingSourceOptions(tagSourcesInternal);
		tagSources.setMinSize(tagSourcesInternal.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		// TODO: whenever the size of something in this changes, trigger a new setMinSize
	}

	public Composite getTagSourcesComposite() {
		return tagSources;
	}
}
