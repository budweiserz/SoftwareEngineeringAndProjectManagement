package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.util.Log;

public class SaalPlanPart {
	
	static Device device = Display.getCurrent ();
	
	private static final Color CSELECTED = new Color (device, 255, 241, 16);
	private static final Color CHOVER = SWTResourceManager.getColor(SWT.COLOR_DARK_YELLOW);
	private static final Color CAVAILABLE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private static final Color CBOOKED = new Color (device, 234, 145, 30);
	private static final Color CSOLD = new Color (device, 234, 30, 30);
	private static final Color CBACKGROUD = new Color (device, 100, 100, 100);
	private Table table;
	private TableColumnLayout tcl_composite;
	private int numOfColumns;
	private int numOfRows;
	
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
		
		
		// TABLE for SAALPLAN
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		
		
		numOfColumns = 10;
		numOfRows = 20;
		
		createColumns();
		createRows();
		
		table.addListener(SWT.MouseDown, new Listener(){
	        public void handleEvent(Event event){
	            Point pt = new Point(event.x, event.y);
	            TableItem item = table.getItem(pt);
	            if(item != null) {
	                for (int column = 0; column < table.getColumnCount(); column++) {
	                	Rectangle rect = item.getBounds(column);
	                    if (rect.contains(pt)) {
	                        Log.info(item);
	                        Color currentColor = item.getBackground(column);
	                        if(currentColor.equals(CAVAILABLE)) {
	                        	item.setBackground(column, CSELECTED);
	                        } else if(currentColor.equals(CSELECTED)) {
	                        	item.setBackground(column, CAVAILABLE);
	                        }
	                        
	                    }
	                }
	            }
	        }
	    });
		
		
		//TABLE END
		
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


	private void createColumns() {
		TableColumn tc;
		int colWidth = (int)Math.floor(650/numOfColumns);
		for(int i =0; i<numOfColumns; i++) {
			tc = new TableColumn(table, SWT.CENTER);
			tcl_composite.setColumnData(tc, new ColumnWeightData(1,colWidth,false));
			tc.setText(String.valueOf(i+1));
		}
	}
	
	private void createRows() {
		TableItem tableItem;
		for(int i=0; i<numOfRows; i++) {
			tableItem = new TableItem(table, SWT.NONE);
			for(int j=0; j<numOfColumns; j++) {
				int rand = (int)Math.floor(Math.random()*4);
				switch(rand) {
				case 0:
					tableItem.setBackground(j, CAVAILABLE);
					break;
				case 1:
					tableItem.setBackground(j, CBOOKED);
					break;
				case 2:
					tableItem.setBackground(j, CBACKGROUD);
					break;
				case 3:
					tableItem.setBackground(j, CSOLD);
					break;
				}
			}
		}
		
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}
