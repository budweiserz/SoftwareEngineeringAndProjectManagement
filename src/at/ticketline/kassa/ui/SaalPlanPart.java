package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;

public class SaalPlanPart {
	private Table table;
	private static final Color CSELECTED = SWTResourceManager.getColor(SWT.COLOR_YELLOW);
	private static final Color CHOVER = SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW);
	private static final Color CAVAILABLE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private static final Color CBOOKED = SWTResourceManager.getColor(SWT.COLOR_GRAY);
	private static final Color CSOLD = SWTResourceManager.getColor(SWT.COLOR_DARK_GRAY);
	private static final Color CBACKGROUD = SWTResourceManager.getColor(SWT.COLOR_BLACK);
	
	public SaalPlanPart() {
		
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(parent, SWT.NONE);
		composite_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_2.setLayout(new FormLayout());
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.minimumHeight = 50;
		gd_composite_2.minimumWidth = 650;
		gd_composite_2.heightHint = 50;
		gd_composite_2.widthHint = 1920;
		composite_2.setLayoutData(gd_composite_2);
		
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		table = new Table(tableComposite, SWT.BORDER | SWT.HIDE_SELECTION | SWT.VIRTUAL | SWT.MULTI);
		table.setLinesVisible(true);
		table.setSize(770, 314);
		table.setTouchEnabled(true);
		
		table.setBackground(CBACKGROUD);
		
		TableColumn column1 = new TableColumn(table, SWT.NONE);
		TableColumn column2 = new TableColumn(table, SWT.NONE);
		TableColumn column3 = new TableColumn(table, SWT.NONE);
		
		TableItem item = new TableItem(table, SWT.NONE);
		item.setBackground(0, CSELECTED);
		item.setBackground(1, CAVAILABLE);
		item.setBackground(2, CSOLD);
		
		item = new TableItem(table, SWT.NONE);
		item.setBackground(CAVAILABLE);
		
		item = new TableItem(table, SWT.NONE);
		item.setBackground(CSOLD);
		
		TableColumnLayout talbeLayout = new TableColumnLayout();
		tableComposite.setLayout(talbeLayout);
		talbeLayout.setColumnData(column1, new ColumnWeightData(33, true));
		talbeLayout.setColumnData(column2, new ColumnWeightData(33, false));
		talbeLayout.setColumnData(column3, new ColumnWeightData(33, false));
		
		column1.pack();
	    column2.pack();
	    column3.pack();
		
		TableCursor tableCursor = new TableCursor(table, SWT.NONE);
		tableCursor.setBackground(CHOVER);
		
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(new FormLayout());
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_composite_1.heightHint = 100;
		gd_composite_1.widthHint = 1920;
		gd_composite_1.minimumHeight = 100;
		gd_composite_1.minimumWidth = 650;
		composite_1.setLayoutData(gd_composite_1);
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}
