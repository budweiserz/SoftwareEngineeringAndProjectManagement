package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.util.Log;

public class SaalPlanPart {
	
	static Device device = Display.getCurrent ();
	
	private static final Color CSELECTED = new Color (device, 255, 241, 16);
	private static final Color CAVAILABLE = SWTResourceManager.getColor(SWT.COLOR_WHITE);
	private static final Color CBOOKED = new Color (device, 234, 145, 30);
	private static final Color CSOLD = new Color (device, 234, 30, 30);
	private static final Color CBACKGROUD = new Color (device, 100, 100, 100);
	private Table table;
	private TableColumnLayout tcl_composite;
	private int numOfColumns;
	private int numOfRows;
	private Text txtPersons;
	private Text txtPrice;
	private int selectedSeats;
	private Spinner spinnerChildren;
	private Label lblSaal;
	
	public SaalPlanPart() {
		selectedSeats = 0;
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		initializeHead(parent);

		initializeContent(parent);
		
		initializeFooter(parent);
	}

	private void initializeHead(Composite parent) {
		GridLayout gl_parent = new GridLayout(1, false);
		parent.setLayout(gl_parent);
		
		Composite composite_2 = new Composite(parent, SWT.NONE);
		composite_2.setLayout(new GridLayout(3, false));
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_composite_2.minimumHeight = 50;
		gd_composite_2.minimumWidth = 650;
		gd_composite_2.heightHint = 50;
		gd_composite_2.widthHint = 1920;
		composite_2.setLayoutData(gd_composite_2);
		
		Composite composite_13 = new Composite(composite_2, SWT.NONE);
		GridData gd_composite_13 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_composite_13.widthHint = 100;
		composite_13.setLayoutData(gd_composite_13);
		
		Label lblVorne = new Label(composite_2, SWT.SEPARATOR | SWT.HORIZONTAL);
		lblVorne.setToolTipText("Bereich Vorne");
		GridData gd_lblVorne = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_lblVorne.widthHint = 600;
		gd_lblVorne.heightHint = 40;
		gd_lblVorne.minimumHeight = 40;
		lblVorne.setLayoutData(gd_lblVorne);
		
		Composite composite_12 = new Composite(composite_2, SWT.NONE);
		GridData gd_composite_12 = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
		gd_composite_12.widthHint = 100;
		composite_12.setLayoutData(gd_composite_12);
		
		lblSaal = new Label(composite_12, SWT.NONE);
		lblSaal.setBounds(24, 0, 66, 14);
		lblSaal.setText("Saal 2");
		
	}

	
	private void initializeContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
		composite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		tcl_composite = new TableColumnLayout();
		composite.setLayout(tcl_composite);
		
		table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION | SWT.MULTI);
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
	                        	selectedSeats++;
	                        	refreshFooter();
	                        } else if(currentColor.equals(CSELECTED)) {
	                        	item.setBackground(column, CAVAILABLE);
	                        	selectedSeats--;
	                        	refreshFooter();
	                        }
	                        
	                    }
	                }
	            }
	        }
	    });
		
		//COLOR EXPLANATION
		Composite composite_3 = new Composite(parent, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(4, false);
		gl_composite_3.horizontalSpacing = 15;
		composite_3.setLayout(gl_composite_3);
		GridData gd_composite_3 = new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1);
		gd_composite_3.minimumWidth = 400;
		gd_composite_3.heightHint = 50;
		gd_composite_3.minimumHeight = 50;
		composite_3.setLayoutData(gd_composite_3);
		
		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayout(new GridLayout(2, false));
		
		Composite composite_5 = new Composite(composite_4, SWT.BORDER);
		GridData gd_composite_5 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_composite_5.widthHint = 20;
		gd_composite_5.minimumWidth = 20;
		gd_composite_5.minimumHeight = 20;
		gd_composite_5.heightHint = 20;
		composite_5.setLayoutData(gd_composite_5);
		composite_5.setBackground(CBOOKED);
		
		Label lblReserviert = new Label(composite_4, SWT.NONE);
		lblReserviert.setText("Reserviert");
		
		Composite composite_6 = new Composite(composite_3, SWT.NONE);
		composite_6.setLayout(new GridLayout(2, false));
		
		Composite composite_7 = new Composite(composite_6, SWT.BORDER);
		GridData gd_composite_7 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_composite_7.widthHint = 20;
		gd_composite_7.minimumWidth = 20;
		gd_composite_7.minimumHeight = 20;
		gd_composite_7.heightHint = 20;
		composite_7.setLayoutData(gd_composite_7);
		composite_7.setBackground(CSOLD);
		
		Label label_1 = new Label(composite_6, SWT.NONE);
		label_1.setText("Verkauft");
		
		Composite composite_8 = new Composite(composite_3, SWT.NONE);
		composite_8.setLayout(new GridLayout(2, false));
		
		Composite composite_9 = new Composite(composite_8, SWT.BORDER);
		GridData gd_composite_9 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_composite_9.widthHint = 20;
		gd_composite_9.minimumWidth = 20;
		gd_composite_9.minimumHeight = 20;
		gd_composite_9.heightHint = 20;
		composite_9.setLayoutData(gd_composite_9);
		composite_9.setBackground(CAVAILABLE);
		
		Label label_2 = new Label(composite_8, SWT.NONE);
		label_2.setText("Frei");
		
		Composite composite_10 = new Composite(composite_3, SWT.NONE);
		composite_10.setLayout(new GridLayout(2, false));
		
		Composite composite_11 = new Composite(composite_10, SWT.BORDER);
		GridData gd_composite_11 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_composite_11.widthHint = 20;
		gd_composite_11.minimumWidth = 20;
		gd_composite_11.minimumHeight = 20;
		gd_composite_11.heightHint = 20;
		composite_11.setLayoutData(gd_composite_11);
		composite_11.setBackground(CSELECTED);
		
		Label label_3 = new Label(composite_10, SWT.NONE);
		label_3.setText("Ausgewählt");
		
	}
	
	
	private void initializeFooter(Composite parent) {
		Composite composite_1 = new Composite(parent, SWT.NONE);
		composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite_1.setLayout(new FormLayout());
		GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_composite_1.heightHint = 100;
		gd_composite_1.widthHint = 1920;
		gd_composite_1.minimumHeight = 100;
		gd_composite_1.minimumWidth = 650;
		composite_1.setLayoutData(gd_composite_1);
		
		Label lblAnzahlDerPersonen = new Label(composite_1, SWT.NONE);
		lblAnzahlDerPersonen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_lblAnzahlDerPersonen = new FormData();
		fd_lblAnzahlDerPersonen.top = new FormAttachment(0, 10);
		fd_lblAnzahlDerPersonen.left = new FormAttachment(0, 10);
		lblAnzahlDerPersonen.setLayoutData(fd_lblAnzahlDerPersonen);
		lblAnzahlDerPersonen.setText("Anzahl der Personen");
		
		Label lblDavonKinder = new Label(composite_1, SWT.NONE);
		lblDavonKinder.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_lblDavonKinder = new FormData();
		fd_lblDavonKinder.top = new FormAttachment(lblAnzahlDerPersonen, 21);
		fd_lblDavonKinder.left = new FormAttachment(0, 10);
		lblDavonKinder.setLayoutData(fd_lblDavonKinder);
		lblDavonKinder.setText("Davon Kinder");
		
		txtPersons = new Text(composite_1, SWT.BORDER);
		txtPersons.setEditable(false);
		FormData fd_txtPersons = new FormData();
		fd_txtPersons.top = new FormAttachment(lblAnzahlDerPersonen, -3, SWT.TOP);
		fd_txtPersons.right = new FormAttachment(lblAnzahlDerPersonen, 66, SWT.RIGHT);
		fd_txtPersons.left = new FormAttachment(lblAnzahlDerPersonen, 24);
		txtPersons.setLayoutData(fd_txtPersons);
		txtPersons.setText(String.valueOf(selectedSeats));
		
		spinnerChildren = new Spinner(composite_1, SWT.BORDER);
		spinnerChildren.setMaximum(0);
		FormData fd_spinnerChildren = new FormData();
		fd_spinnerChildren.top = new FormAttachment(lblDavonKinder, -4, SWT.TOP);
		fd_spinnerChildren.right = new FormAttachment(txtPersons, 42);
		fd_spinnerChildren.left = new FormAttachment(txtPersons, 0, SWT.LEFT);
		spinnerChildren.setLayoutData(fd_spinnerChildren);
		
		spinnerChildren.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshFooter();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//Nothing to do here
				
			}
		});
		
		Label lblPreis = new Label(composite_1, SWT.NONE);
		lblPreis.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_lblPreis = new FormData();
		fd_lblPreis.bottom = new FormAttachment(lblAnzahlDerPersonen, 0, SWT.BOTTOM);
		fd_lblPreis.left = new FormAttachment(txtPersons, 73);
		lblPreis.setLayoutData(fd_lblPreis);
		lblPreis.setText("Preis");
		
		txtPrice = new Text(composite_1, SWT.BORDER);
		txtPrice.setEditable(false);
		FormData fd_txtPrice = new FormData();
		fd_txtPrice.right = new FormAttachment(lblPreis, 104, SWT.RIGHT);
		fd_txtPrice.top = new FormAttachment(0, 7);
		fd_txtPrice.left = new FormAttachment(lblPreis, 44);
		txtPrice.setLayoutData(fd_txtPrice);
		txtPrice.setText(String.valueOf(selectedSeats*10));
		
		Button btnReservieren = new Button(composite_1, SWT.NONE);
		FormData fd_btnReservieren = new FormData();
		fd_btnReservieren.top = new FormAttachment(lblAnzahlDerPersonen, 0, SWT.TOP);
		fd_btnReservieren.right = new FormAttachment(100, -10);
		btnReservieren.setLayoutData(fd_btnReservieren);
		btnReservieren.setText("Reservieren");
		
		btnReservieren.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Log.info("Button reservieren wurde gedrückt!");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//Nothing to do here
				
			}
		});
		
		Button btnKaufen = new Button(composite_1, SWT.NONE);
		FormData fd_btnKaufen = new FormData();
		fd_btnKaufen.top = new FormAttachment(lblDavonKinder, -5, SWT.TOP);
		fd_btnKaufen.right = new FormAttachment(btnReservieren, 0, SWT.RIGHT);
		btnKaufen.setLayoutData(fd_btnKaufen);
		btnKaufen.setText("Kaufen");
		
		btnKaufen.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Log.info("Button kaufen wurde gedrückt!");
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//Nothing to do here
				
			}
		});
		
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

	private void refreshFooter() {
		txtPersons.setText(String.valueOf(selectedSeats));
		spinnerChildren.setMaximum(selectedSeats);
		// Calculate Price
		int children= Integer.valueOf(spinnerChildren.getText());
		double pps = 9.50;
		double ppsChildren = 4.90;
		double price = (selectedSeats - children) * pps + children * ppsChildren;
		txtPrice.setText(String.valueOf(price));
	}
	
	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		spinnerChildren.setFocus();
	}
}
