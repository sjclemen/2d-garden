package ca.twodee.ui;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateSetStrategy;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.set.IObservableSet;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import ca.twodee.ui.helpers.DataBindingHelper;
import ca.twodee.ui.helpers.ImageDataToImageConverter;
import ca.twodee.ui.helpers.IssueViewerComparator;
import ca.twodee.ui.helpers.ObservableMapLabelProviderWithTagSupport;
import ca.twodee.ui.helpers.IssueViewerComparator.CompareTarget;
import ca.twodee.ui.pieces.IssueFilter;
import ca.twodee.ui.pieces.IssueOverview;
import ca.twodee.ui.pieces.SearchArea;
import ca.twodee.ui.pieces.tags.TagListVMBinding;
import ca.twodee.ui.tagfilling.UiTagfillingMainArea;
import ca.twodee.ui.viewmodel.IssueListVM;
import ca.twodee.ui.viewmodel.IssueVM;
import ca.twodee.ui.viewmodel.TagVM;

/**
 * Handles all databinding for the UI. Accepts a viewmodel at creation and then binds each piece
 * of the UI individually.
 */
public class DataBinding {
	private final IssueListVM viewModel;
	private TableViewer issueViewer;
	private DataBindingContext detailContext;
	private final IssueFilter filter;
	private IObservableValue selection; // selected item in table
	
	public DataBinding(IssueListVM viewModel) {
		this.viewModel = viewModel;
		this.filter = new IssueFilter();
	}
	
	public IObservableValue getSelection() {
		return selection;
	}

	public void bindTable(Table table) {
		issueViewer = new TableViewer(table);
		
		ObservableListContentProvider issueListContentProvider = new ObservableListContentProvider();
		issueViewer.setContentProvider(issueListContentProvider);
		
		IObservableMap nameMap = BeansObservables.observeMap(issueListContentProvider.getKnownElements(), IssueVM.class, "name");
		IObservableMap tagMap = BeansObservables.observeMap(issueListContentProvider.getKnownElements(), IssueVM.class, "issueTags");
		IObservableMap ratingMap = BeansObservables.observeMap(issueListContentProvider.getKnownElements(), IssueVM.class, "rating");
		
		IObservableMap[] columnMaps = new IObservableMap[] {
				nameMap, tagMap, ratingMap
		};
		
		issueViewer.setComparator(new IssueViewerComparator());
		table.getColumns()[0].addSelectionListener(new IssueViewerComparator.ColumnSelectionListener(issueViewer, CompareTarget.NAME));
		table.getColumns()[1].addSelectionListener(new IssueViewerComparator.ColumnSelectionListener(issueViewer, CompareTarget.TAGS));
		table.getColumns()[2].addSelectionListener(new IssueViewerComparator.ColumnSelectionListener(issueViewer, CompareTarget.RATING));
		
		selection = ViewersObservables.observeSingleSelection(issueViewer);
		
		issueViewer.addDoubleClickListener(new IDoubleClickListener() {
			private final Logger logger = Logger.getLogger("IDoubleClickListener anonymous");

			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				if (!arg0.getSelection().isEmpty()) {
					IssueVM issueVM = (IssueVM) selection.getValue();
					try {
						Runtime.getRuntime().exec(new String[] { "mcomix", issueVM.getArchiveVM().getFullPath() });
					} catch (IOException e) {
						logger.error("Failed to start comic application", e);
					}
				}
			}
			
		});
		
		issueViewer.setLabelProvider(new ObservableMapLabelProviderWithTagSupport(columnMaps));
		issueViewer.setInput(viewModel.getIssueWritable());
		issueViewer.addFilter(filter);
		filter.setViewer(issueViewer);
	}
	
	public void bindSearch(SearchArea searchArea) {
		if (issueViewer == null) {
			throw new IllegalArgumentException("Must call bind search after bind table");
		}
		
		searchArea.getSearchText().addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent arg0) {
				filter.setSearchText(searchArea.getSearchText().getText());
			}
			
		});
		
		TagListVMBinding tagListBinding = new TagListVMBinding(viewModel, searchArea.getTagList());
		filter.bindIssueTags(searchArea.getTagList().getSelectedTags());
		
		searchArea.getTagAllRadio().setSelection(true);
		
		searchArea.getTagAllRadio().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				filter.setIssueTagOperation(true);
			}
			
		});
		
		searchArea.getTagAnyRadio().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				filter.setIssueTagOperation(false);
			}
			
		});
	}
	
	public static void bindDetailView(IssueOverview overview, IssueListVM viewModel, IObservableValue selection) {		
		if (selection == null) {
			throw new IllegalArgumentException("Must call bind detail after bind table");
		}
				
		DataBindingContext detailContext = new DataBindingContext();
		
		DataBindingHelper.bindTextToName(detailContext, selection, overview.getNameText());
		
		IObservableValue ratingObservable = BeanProperties.value((Class) selection.getValueType(), "rating",
				Integer.class)
				.observeDetail(selection);
		Binding ratingBinding = detailContext.bindValue(WidgetProperties.selection().observe(overview.getStarsSpinner()), ratingObservable,
				new UpdateValueStrategy(false,
						UpdateValueStrategy.POLICY_ON_REQUEST), null);
		
		TagListVMBinding tagListBinding = new TagListVMBinding(viewModel, overview.getTagsText());
		
		IObservableSet tagSetObservable = BeanProperties.set((Class) selection.getValueType(), "issueTags", TagVM.class).observeDetail(selection);
		Binding tagBinding = detailContext.bindSet(overview.getTagsText().getSelectedTags(), tagSetObservable, new UpdateSetStrategy(false,
						UpdateSetStrategy.POLICY_UPDATE), null);
//		Binding tagBinding = detailContext.bindSet(overview.getTagsText().getSelectedTags(), tagSetObservable, null, null);
		
		IObservableValue thumbnailObservable = BeanProperties.value((Class) selection.getValueType(), "archiveVM.thumbnailImageData")
				.observeDetail(selection);
		
		UpdateValueStrategy thumbnailStrategy = new UpdateValueStrategy(false, UpdateValueStrategy.POLICY_UPDATE);
		thumbnailStrategy.setConverter(new ImageDataToImageConverter(overview.getComposite().getDisplay()));
		Binding thumbnailBinding = detailContext.bindValue(
				SWTObservables.observeImage(overview.getImageLabel()),
				thumbnailObservable,
				new UpdateValueStrategy(false, UpdateValueStrategy.POLICY_NEVER),
				thumbnailStrategy);

		// various events need to be manually performed on a selection change
		
		IValueChangeListener selectionValueChangeListener = new IValueChangeListener() {

			@Override
			public void handleValueChange(ValueChangeEvent event) {
				Object old = event.diff.getOldValue();
				if (old != null) {
					IssueVM ivm = (IssueVM)old;
					ivm.flush();
				}

				overview.getComposite().layout();
				overview.getScrolledComposite().setMinSize(overview.getComposite().computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
			
		};
		selection.addValueChangeListener(selectionValueChangeListener);
		overview.getScrolledComposite().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				selection.removeValueChangeListener(selectionValueChangeListener);
			}
			
		});
		
		
		IChangeListener selectionChangeListener = new IChangeListener() {

			@Override
			public void handleChange(ChangeEvent arg0) {
				if (overview.getImageLabel().getImage() != null) {
					overview.getImageLabel().getImage().dispose();
					overview.getImageLabel().setImage(null);
				}
				detailContext.updateModels();
				overview.getTagsText().getTagListCombo().clearText();
			}
			
		};
		selection.addChangeListener(selectionChangeListener);
		overview.getScrolledComposite().addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent arg0) {
				selection.removeChangeListener(selectionChangeListener);
			}
			
		});

		overview.getFillTagsButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				Shell fillTagsShell = new Shell(overview.getFillTagsButton().getDisplay());
				UiTagfillingMainArea mainArea = new UiTagfillingMainArea(fillTagsShell);
				fillTagsShell.setLayout(new FillLayout());
				bindTagfilling(mainArea, viewModel, selection);
				fillTagsShell.pack();
				fillTagsShell.open();
			}
			
		});
		
	}
	
	public static void bindTagfilling(UiTagfillingMainArea mainArea, IssueListVM viewModel, IObservableValue selection) {
		bindDetailView(mainArea.getIssueOverview(), viewModel, selection);

	}
}
