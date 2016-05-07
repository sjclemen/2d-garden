package ca.twodee.ui.pieces;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class IssueList {
	private final Composite tableContainer;
	private final Table table;
	
	public IssueList(Composite parent) {
		tableContainer = new Composite(parent, SWT.NONE);
		final GridData gridDataTableArea = new GridData();
		gridDataTableArea.grabExcessVerticalSpace = true;
		gridDataTableArea.verticalAlignment = GridData.FILL;
		gridDataTableArea.grabExcessHorizontalSpace = true;
		gridDataTableArea.horizontalAlignment = GridData.FILL;
		tableContainer.setLayoutData(gridDataTableArea);
		
		TableColumnLayout layout = new TableColumnLayout();
		tableContainer.setLayout(layout);
		
		table = new Table(tableContainer, SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER);
		table.setHeaderVisible(true);
		final TableColumn columnName = new TableColumn(table, SWT.NONE);
		columnName.setText("Name");
		final TableColumn columnTags = new TableColumn(table, SWT.NONE);
		columnTags.setText("Tags");
		final TableColumn columnRating = new TableColumn(table, SWT.NONE);
		columnRating.setText("Rating");
		
		layout.setColumnData(columnName, new ColumnWeightData(3));
		layout.setColumnData(columnTags, new ColumnWeightData(1));
		layout.setColumnData(columnRating, new ColumnPixelData(75));
		
		table.layout();
	}
	
	public Table getTable() {
		return table;
	}
}
