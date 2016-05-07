package ca.twodee.ui.tagfilling;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import ca.twodee.ui.RealmHelper;

public class TagfillingMain {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		
		Realm realm = SWTObservables.getRealm(display);
		@SuppressWarnings("unused")
		RealmHelper rh = new RealmHelper(realm);

		@SuppressWarnings("unused")
		UiTagfillingMainArea mainArea = new UiTagfillingMainArea(shell);
		shell.setLayout(new FillLayout());
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

}
