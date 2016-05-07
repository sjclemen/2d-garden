package ca.twodee.ui.pieces.tags;

import org.eclipse.core.databinding.observable.set.WritableSet;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public interface TagListSelectedInterface {
	public WritableSet getSelectedTags();
	public Composite getWidget();
	public Shell getShell();
}
