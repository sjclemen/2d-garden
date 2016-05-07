package ca.twodee.ui.pieces;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.google.common.eventbus.AsyncEventBus;

import ca.twodee.backend.manager.BackendConnection;
import ca.twodee.backend.manager.BackendListenableFutureTask;
import ca.twodee.persistence.message.archive.GetArchiveRepositoriesRequest;
import ca.twodee.persistence.message.archive.GetArchivesRequest;
import ca.twodee.persistence.message.issue.GetIssuesRequest;
import ca.twodee.persistence.message.tag.GetTagCategoriesRequest;
import ca.twodee.persistence.message.tag.GetTagsRequest;
import ca.twodee.schema.Archive;
import ca.twodee.schema.ArchiveRepository;
import ca.twodee.schema.Issue;
import ca.twodee.schema.Tag;
import ca.twodee.schema.TagCategory;
import ca.twodee.ui.DataBinding;
import ca.twodee.ui.RealmHelper;
import ca.twodee.ui.viewmodel.IssueListVM;

public class MainWindow {
	private final static Logger logger = Logger.getLogger(MainWindow.class);
	private final Display display;
	private final Shell shell;
	
	private final IssueOverview issueOverview;
	private final BackendConnection connection;
	private SearchArea searchArea;
	private IssueList issueList;
	
	private IssueListVM viewModel;
	private Realm realm;
	
	public MainWindow(Display display, BackendConnection connection, AsyncEventBus eventBus) {
		this.display = display;
		this.connection = connection;
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setText("2D Garden");

		makeMenu(shell, display);

		final Composite mainHorizontal = new Composite(shell, SWT.BORDER);
		final GridLayout mainHorizontalLayout = new GridLayout();
		mainHorizontalLayout.numColumns = 2;
		mainHorizontal.setLayout(mainHorizontalLayout);

		Realm realm = SWTObservables.getRealm(display);
		@SuppressWarnings("unused")
		RealmHelper rh = new RealmHelper(realm);
		
		GetIssuesRequest gir = new GetIssuesRequest();
		BackendListenableFutureTask<List<Issue>> issueItems = connection.enqueueTask(gir);
		GetTagsRequest gtl = new GetTagsRequest();
		BackendListenableFutureTask<List<Tag>> tagItems = connection.enqueueTask(gtl);
		GetTagCategoriesRequest gtcr = new GetTagCategoriesRequest();
		BackendListenableFutureTask<List<TagCategory>> tagCategoryItems = connection.enqueueTask(gtcr);
		GetArchivesRequest gar = new GetArchivesRequest();
		BackendListenableFutureTask<List<Archive>> archiveItems = connection.enqueueTask(gar);
		GetArchiveRepositoriesRequest garr = new GetArchiveRepositoriesRequest();
		BackendListenableFutureTask<List<ArchiveRepository>> archiveRepositoryItems = connection.enqueueTask(garr);
		try {
			viewModel = new IssueListVM(issueItems.get(), tagItems.get(), tagCategoryItems.get(), archiveItems.get(), archiveRepositoryItems.get(), connection, eventBus);
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Failed to create view model", e);
			throw new RuntimeException("Failed to create view model", e);
		}
		
		makeLeftSide(mainHorizontal);
		issueOverview = new IssueOverview(mainHorizontal);
		
		GridData gridDataRightSideScrollable = new GridData();
		gridDataRightSideScrollable.horizontalAlignment = GridData.END;
		gridDataRightSideScrollable.grabExcessVerticalSpace = true;
		gridDataRightSideScrollable.verticalAlignment = GridData.FILL;
		gridDataRightSideScrollable.widthHint = 375;
		issueOverview.getScrolledComposite().setLayoutData(gridDataRightSideScrollable);
		
		mainHorizontal.layout();
		
		DataBinding dataBindingSetup = new DataBinding(viewModel);
		dataBindingSetup.bindTable(issueList.getTable());
		dataBindingSetup.bindSearch(searchArea);
		dataBindingSetup.bindDetailView(issueOverview, viewModel, dataBindingSetup.getSelection());
		
		shell.setMinimumSize(740, 480);
		
	}
	
	public void mainLoop() {
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	
	private void makeLeftSide(Composite mainHorizontal) {
		final Composite leftSide = new Composite(mainHorizontal, SWT.BORDER);
		GridLayout leftSideLayout = new GridLayout();
		leftSideLayout.numColumns = 1;
		leftSide.setLayout(leftSideLayout);
		final GridData gridDataLeftSide = new GridData();
		gridDataLeftSide.grabExcessHorizontalSpace = true;
		gridDataLeftSide.horizontalAlignment = GridData.FILL;
		gridDataLeftSide.grabExcessVerticalSpace = true;
		gridDataLeftSide.verticalAlignment = GridData.FILL;
		leftSide.setLayoutData(gridDataLeftSide);
		
		searchArea = new SearchArea(leftSide);
		issueList = new IssueList(leftSide);

		leftSide.layout();
	}

	private static void makeMenu(Shell shell, Display display) {
		// add a menu
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		MenuItem fileItem = new MenuItem(bar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);
		MenuItem item = new MenuItem(submenu, SWT.PUSH);
		item.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				display.dispose();
			}
		});
		item.setText("Quit\tCtrl+Q");
		item.setAccelerator(SWT.MOD1 + 'Q');
	}

}
