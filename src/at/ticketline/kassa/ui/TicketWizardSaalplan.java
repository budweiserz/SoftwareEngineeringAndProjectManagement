package at.ticketline.kassa.ui;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;


@SuppressWarnings("restriction")
public class TicketWizardSaalplan extends WizardPage implements Listener {

    private Composite container;
    private TicketWizardValues values;
    private Saal saal;
    private Reihe[] reihen;
    private HashMap<Integer, Platz> plaetze;
    private HashMap<Integer, Platz> ausgewaehltePlaetze;
    private static final Logger LOG = LoggerFactory.getLogger(TicketWizardSaalplan.class);
    
    //Saalplan
    static Device device = Display.getCurrent ();
    
    private static final Color CSELECTED = new Color (device, 0, 220, 255);
    private static final Color CAVAILABLE = new Color (device, 255, 255, 255);
    private static final Color CBOOKED = new Color (device, 250, 176, 87);
    private static final Color CSOLD = new Color (device, 250, 124, 87);
    private static final Color CBACKGROUD = new Color (device, 206, 206, 206);
    private static final Color CWHITE= new Color(device, 255, 255, 255);
    private Table table;
    private TableColumnLayout tcl_composite;
    private int numOfColumns;
    private int numOfRows;
    private Text txtPersons;
    private Text txtPrice;
    private int selectedSeats;
    private Label lblSaal;

    
    /**
     * Diese Seite stellt einen grafischen Saalplan zur Auswahl
     * der Plätze zur Verfügung sowie die Möglichkeit zwischen Reservierung
     * und Buchung zu entscheiden
     */
    public TicketWizardSaalplan(TicketWizardValues values2) {
        super("saalplan");
        setTitle("Verfügbare Plätze");
        setDescription("Wählen Sie die gewünschten Plätze und die Art der Transaktion aus");
        this.values = values2;
        saal = values2.getAuffuehrung().getSaal();
        reihen = saal.getReihen().toArray(new Reihe[saal.getReihen().size()]);
        selectedSeats = 0;
        plaetze = new HashMap<Integer, Platz>();
        for(Platz platz : values2.getAuffuehrung().getPlaetze()) {
        	plaetze.put(platz.getNummer(), platz);
        }
        ausgewaehltePlaetze = new HashMap<Integer, Platz>();
        setPageComplete(true);
        
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        container = new Composite(parent, SWT.NULL);

        
        setControl(container);
        
        //initialize Saalplan
        initializeHead(container);

        initializeContent(container);
        
        initializeFooter(container);
        
        fillWithTransaction();
              
        //TODO make true when plaetze selected
        LOG.info("Wizardseite zum Auswahl der Plätze und Transaktionsart erstellt!");

    }

	@Override
    public void handleEvent(Event e) {
        values.setReservierung(false);
        ((TicketWizard) getWizard()).fuenf.updateContent();
    }
    
    private void initializeHead(Composite parent) {
        GridLayout gl_parent = new GridLayout(1, false);
        parent.setLayout(gl_parent);
        
        Composite composite_2 = new Composite(parent, SWT.NONE);
        composite_2.setLayout(new GridLayout(3, false));
        GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_composite_2.minimumHeight = 60;
        gd_composite_2.minimumWidth = 700;
        gd_composite_2.heightHint = 60;
        gd_composite_2.widthHint = 1920;
        composite_2.setLayoutData(gd_composite_2);
        
        Composite composite_13 = new Composite(composite_2, SWT.NONE);
        GridData gd_composite_13 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_composite_13.minimumHeight = 40;
        gd_composite_13.heightHint = 40;
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
        composite_12.setLayout(new GridLayout(1, false));
        GridData gd_composite_12 = new GridData(SWT.CENTER, SWT.CENTER, false, true, 1, 1);
        gd_composite_12.minimumHeight = 40;
        gd_composite_12.heightHint = 40;
        gd_composite_12.widthHint = 100;
        composite_12.setLayoutData(gd_composite_12);
        
        lblSaal = new Label(composite_12, SWT.NONE);
        lblSaal.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
        lblSaal.setText(saal.getBezeichnung());
        
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
        
        numOfColumns = 0;
        for(Reihe reihe : reihen) {
        	numOfColumns = Math.max(numOfColumns, reihe.getAnzplaetze());
        }
        numOfRows = reihen.length;
        
        createColumns();
        createRows();
        
        
        table.addListener(SWT.MouseDown, new Listener(){
            @Override public void handleEvent(Event event) {
                table.deselectAll();
            }
        });
        table.addListener(SWT.MouseUp, new Listener(){
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
                            	Platz platz = new Platz();
                            	platz.setNummer(table.indexOf(item)*numOfColumns + column);
                            	ausgewaehltePlaetze.put(platz.getNummer(), platz);
                            	values.setPlaetze(mapToSet(ausgewaehltePlaetze));
                                item.setBackground(column, CSELECTED);
                                selectedSeats++;
                                refreshFooter();
                            } else if(currentColor.equals(CSELECTED)) {
                            	item.setBackground(column, CAVAILABLE);
                                ausgewaehltePlaetze.remove(table.indexOf(item)*numOfColumns + column);
                                values.setPlaetze(mapToSet(ausgewaehltePlaetze));
                                selectedSeats--;
                                refreshFooter();
                            }
                        }
                    }
                }
                table.deselectAll();
            }
        });
        
        //COLOR EXPLANATION
        Composite composite_3 = new Composite(parent, SWT.NONE);
        GridLayout gl_composite_3 = new GridLayout(5, false);
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
        
        Composite composite_1 = new Composite(composite_3, SWT.NONE);
        composite_1.setLayout(new GridLayout(2, false));
        
        Composite composite_2 = new Composite(composite_1, SWT.BORDER);
        GridData gd_composite_2 = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
        gd_composite_2.widthHint = 20;
        gd_composite_2.minimumWidth = 20;
        gd_composite_2.minimumHeight = 20;
        gd_composite_2.heightHint = 20;
        composite_2.setLayoutData(gd_composite_2);
        composite_2.setBackground(CBACKGROUD);
        
        Label label = new Label(composite_1, SWT.NONE);
        label.setText("Inaktiv");
        
    }
    
    
    private void initializeFooter(Composite parent) {
        Composite composite_1 = new Composite(parent, SWT.NONE);
        composite_1.setBackground(CWHITE);
        composite_1.setLayout(new FormLayout());
        GridData gd_composite_1 = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_composite_1.heightHint = 70;
        gd_composite_1.widthHint = 1920;
        gd_composite_1.minimumHeight = 70;
        gd_composite_1.minimumWidth = 650;
        composite_1.setLayoutData(gd_composite_1);
        
        Label lblAnzahlDerPersonen = new Label(composite_1, SWT.NONE);
        lblAnzahlDerPersonen.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        FormData fd_lblAnzahlDerPersonen = new FormData();
        fd_lblAnzahlDerPersonen.top = new FormAttachment(0, 10);
        fd_lblAnzahlDerPersonen.left = new FormAttachment(0, 10);
        lblAnzahlDerPersonen.setLayoutData(fd_lblAnzahlDerPersonen);
        lblAnzahlDerPersonen.setText("Anzahl der Personen");
        
        txtPersons = new Text(composite_1, SWT.BORDER);
        txtPersons.setEditable(false);
        FormData fd_txtPersons = new FormData();
        fd_txtPersons.top = new FormAttachment(lblAnzahlDerPersonen, -3, SWT.TOP);
        fd_txtPersons.right = new FormAttachment(lblAnzahlDerPersonen, 66, SWT.RIGHT);
        fd_txtPersons.left = new FormAttachment(lblAnzahlDerPersonen, 24);
        txtPersons.setLayoutData(fd_txtPersons);
        txtPersons.setText(String.valueOf(selectedSeats));

        FormData fd_spinnerChildren = new FormData();
        fd_spinnerChildren.right = new FormAttachment(txtPersons, 42);
        fd_spinnerChildren.left = new FormAttachment(txtPersons, 0, SWT.LEFT);
        
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
        
        
    }
    
    private void createColumns() {
    	TableColumn tc;
        int colWidth = (int)Math.floor((700-table.getBorderWidth()*2)/numOfColumns);
        for(int i =0; i<numOfColumns; i++) {
            tc = new TableColumn(table, SWT.CENTER);
            tcl_composite.setColumnData(tc, new ColumnWeightData(1,colWidth,false));
            tc.setText(String.valueOf(i+1));
        }
    }
    
    private void createRows() {
        TableItem tableItem;
        Reihe reihe;
        Platz platz;
        for(int i=0; i<numOfRows; i++) {
            tableItem = new TableItem(table, SWT.NONE);
            reihe = reihen[i];
            for(int j=0; j<numOfColumns; j++) {
            	if(j<reihe.getAnzplaetze()) {
            		platz = plaetze.get(i*numOfColumns + j);
                	
                	if(platz != null) {
    	            	PlatzStatus status = platz.getStatus();
    	                switch(status) {
    	                case FREI:
    	                    tableItem.setBackground(j, CAVAILABLE);
    	                    break;
    	                case RESERVIERT:
    	                    tableItem.setBackground(j, CBOOKED);
    	                    break;
    	                case GEBUCHT:
    	                    tableItem.setBackground(j, CSOLD);
    	                    break;
    	                }
                	} else {
                		tableItem.setBackground(j, CAVAILABLE);
                	}
            	} else {
            		//No available Seat here
            		tableItem.setBackground(CBACKGROUD);
            	}
            	
            }
        }
    }

    private void refreshFooter() {
        txtPersons.setText(String.valueOf(selectedSeats));
        // Calculate Price
        double pps = 9.50;
        double price = selectedSeats*pps;
        txtPrice.setText(String.valueOf(price));
    }
    
    private void fillWithTransaction() {
    	values.setKunde(values.getTransaktion().getKunde());
    	for(Platz platz: values.getPlaetze()) {
        	ausgewaehltePlaetze.put(platz.getNummer(), platz);
        	int numRow = (int)Math.floor(platz.getNummer()/numOfColumns);
            TableItem item = table.getItem(numRow);
        	item.setBackground((platz.getNummer() - numRow * numOfColumns), CSELECTED);
            selectedSeats++;
		}
		refreshFooter();
		values.setPlaetze(mapToSet(ausgewaehltePlaetze));
		LOG.debug("Plaetze der Vorherigen Buchung: " + ausgewaehltePlaetze);
		
	}
    
    private HashSet<Platz> mapToSet(HashMap<Integer, Platz> map) {
    	Iterator<Integer> it = map.keySet().iterator();
    	HashSet<Platz> set = new HashSet<Platz>();
    	while(it.hasNext()) {
    		set.add(map.get(it.next()));
    	}
    	
    	return set;
    }
    
    @PreDestroy
    public void dispose() {
    }
}