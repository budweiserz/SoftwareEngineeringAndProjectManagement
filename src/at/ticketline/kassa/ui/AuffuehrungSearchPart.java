package at.ticketline.kassa.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.AuffuehrungService;
import org.eclipse.swt.widgets.Combo;

@SuppressWarnings("restriction")
public class AuffuehrungSearchPart {
    private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungsortSearchPart.class);
    
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    //@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    //@Inject private Ort ort;
    @Inject private AuffuehrungService auffuehrungService;
    
    TableViewer tableViewer;
    DateTime dateTimeFrom;
    DateTime dateTimeTo;
	private Table table;
	private Text text_2;
	private Text text_3;
	Combo preiskategorie;
	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite SearchComposite = new Composite(parent, SWT.BORDER);
		SearchComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		SearchComposite.setLayout(new FormLayout());
		GridData gd_SearchComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_SearchComposite.heightHint = 100;
		gd_SearchComposite.widthHint = 1920;
		gd_SearchComposite.minimumHeight = 100;
		gd_SearchComposite.minimumWidth = 750;
		SearchComposite.setLayoutData(gd_SearchComposite);
		
		Button btnSuchen = new Button(SearchComposite, SWT.NONE);
		FormData fd_btnSuchen = new FormData();
		fd_btnSuchen.left = new FormAttachment(100, -67);
		fd_btnSuchen.bottom = new FormAttachment(100, -10);
		fd_btnSuchen.right = new FormAttachment(100, -10);
		btnSuchen.setLayoutData(fd_btnSuchen);
		btnSuchen.setText("suchen");
		
		Label lblDatum = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblDatum = new FormData();
		fd_lblDatum.top = new FormAttachment(0, 10);
		fd_lblDatum.left = new FormAttachment(0, 10);
		lblDatum.setLayoutData(fd_lblDatum);
		lblDatum.setText("Datum");
		
		dateTimeFrom = new DateTime(SearchComposite, SWT.BORDER);
		
	      
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
		//set from time one year in the past
        year -=1;
		dateTimeFrom.setToolTipText("Von");
		dateTimeFrom.setDate(year, month, day);
		FormData fd_dateTimeFrom = new FormData();
		fd_dateTimeFrom.top = new FormAttachment(lblDatum, -5, SWT.TOP);
		fd_dateTimeFrom.left = new FormAttachment(lblDatum, 34);
		dateTimeFrom.setLayoutData(fd_dateTimeFrom);
		
		Label label = new Label(SearchComposite, SWT.NONE);
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(lblDatum, 0, SWT.TOP);
		fd_label.left = new FormAttachment(dateTimeFrom, 22);
		label.setLayoutData(fd_label);
		label.setText("-");
		

		//add one to day to include today's generated dates
		year += 1;
		day+=1;
		
		dateTimeTo = new DateTime(SearchComposite, SWT.BORDER);
		dateTimeTo.setDate(year, month, day);
		dateTimeTo.setToolTipText("Bis");
		FormData fd_dateTimeTo = new FormData();
		fd_dateTimeTo.top = new FormAttachment(label, -5, SWT.TOP);
		fd_dateTimeTo.left = new FormAttachment(label, 26);
		dateTimeTo.setLayoutData(fd_dateTimeTo);
		
		Label lblPreis = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblPreis = new FormData();
		fd_lblPreis.top = new FormAttachment(lblDatum, 27);
		fd_lblPreis.left = new FormAttachment(lblDatum, 0, SWT.LEFT);
		lblPreis.setLayoutData(fd_lblPreis);
		lblPreis.setText("Preis");
		
		Label lblSaal = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblSaal = new FormData();
		fd_lblSaal.bottom = new FormAttachment(lblDatum, 0, SWT.BOTTOM);
		fd_lblSaal.left = new FormAttachment(dateTimeTo, 101);
		lblSaal.setLayoutData(fd_lblSaal);
		lblSaal.setText("Saal");
		
		Label lblVeranstaltung = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblVeranstaltung = new FormData();
		fd_lblVeranstaltung.bottom = new FormAttachment(lblPreis, 0, SWT.BOTTOM);
		lblVeranstaltung.setLayoutData(fd_lblVeranstaltung);
		lblVeranstaltung.setText("Veranstaltung");
		
		text_2 = new Text(SearchComposite, SWT.BORDER);
		FormData fd_text_2 = new FormData();
		fd_text_2.right = new FormAttachment(lblSaal, 206, SWT.RIGHT);
		fd_text_2.top = new FormAttachment(0, 7);
		fd_text_2.left = new FormAttachment(lblSaal, 95);
		text_2.setLayoutData(fd_text_2);
		
		text_3 = new Text(SearchComposite, SWT.BORDER);
		fd_lblVeranstaltung.right = new FormAttachment(text_3, -33);
		FormData fd_text_3 = new FormData();
		fd_text_3.right = new FormAttachment(text_2, 0, SWT.RIGHT);
		fd_text_3.left = new FormAttachment(0, 492);
		fd_text_3.top = new FormAttachment(lblVeranstaltung, -3, SWT.TOP);
		text_3.setLayoutData(fd_text_3);
		
		preiskategorie = new Combo(SearchComposite, SWT.NONE);
		preiskategorie.setItems(new String[] {"Mindestpreis", "Standardpreis", "Maximalpreis"});
		FormData fd_preiskategorie = new FormData();
		fd_preiskategorie.right = new FormAttachment(dateTimeTo, 0, SWT.RIGHT);
		fd_preiskategorie.top = new FormAttachment(dateTimeFrom, 15);
		fd_preiskategorie.left = new FormAttachment(lblPreis, 45);
		preiskategorie.setLayoutData(fd_preiskategorie);
		SearchComposite.setTabList(new Control[]{btnSuchen});
		
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnVeranstaltung = tableViewerColumn.getColumn();
		tblclmnVeranstaltung.setWidth(150);
		tblclmnVeranstaltung.setText("Veranstaltung");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnPreis = tableViewerColumn_1.getColumn();
		tblclmnPreis.setWidth(102);
		tblclmnPreis.setText("Preis in €");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn Datum = tableViewerColumn_2.getColumn();
		Datum.setWidth(111);
		Datum.setText("Datum");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnSaal = tableViewerColumn_3.getColumn();
		tblclmnSaal.setWidth(80);
		tblclmnSaal.setText("Saal");
		
        tableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                // The inputElement comes from view.setInput()
                if (inputElement instanceof List) {
                    List models = (List)inputElement;
                    return models.toArray();
                }
                return new Object[0];
            }
            @Override public void dispose() { }
            @Override public void inputChanged(Viewer viewer, Object oldInput, Object newInput) { }
        });
        
        tableViewer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
            @Override
            public String getColumnText(Object element, int index) {
                Auffuehrung e = (Auffuehrung) element;
                switch (index) {
                case 0:
                    if (e.getVeranstaltung() != null) {
                        return e.getVeranstaltung().getBezeichnung();
                    } else {
                        return "";
                    }
                case 1:
                    if (e.getPreis() != null) {
                        return e.getPreis().toString();
                    } else {
                        return "";
                    }
                case 2:
                    if (e.getDatumuhrzeit() != null) {
                        SimpleDateFormat format = new SimpleDateFormat("dd:MM:yyyy");
                    	return format.format(e.getDatumuhrzeit());
                    } else {
                        return "";
                    }
                case 3:
                    if (e.getSaal() != null) {
                        return e.getSaal().getBezeichnung();
                    } else {
                        return "";
                    }
                }
                return null;
            }
    
            @Override
            public void addListener(ILabelProviderListener listener) {
                // nothing to do
            }
    
            @Override
            public void dispose() {
                // nothing to do
            }
    
            @Override
            public boolean isLabelProperty(Object arg0, String arg1) {
                return true;
            }
    
            @Override
            public void removeListener(ILabelProviderListener arg0) {
                // nothing to do
            }
        });
        
        btnSuchen.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }

            @Override
            public void mouseUp(MouseEvent e) {
                
            	Auffuehrung query = new Auffuehrung();
            	Auffuehrung queryto = new Auffuehrung();
            	query.setSaal(new Saal());
            	query.getSaal().setBezeichnung(text_2.getText().length() > 0 ? text_2.getText() : null);
            	query.setVeranstaltung(new Veranstaltung());
            	query.getVeranstaltung().setBezeichnung(text_3.getText().length() > 0 ? text_3.getText() : null);
            	PreisKategorie pk = null;
            	switch (preiskategorie.getSelectionIndex()) {
                case 0:
                    pk = PreisKategorie.MINDESTPREIS;
                    break;
                case 1:
                    pk = PreisKategorie.STANDARDPREIS;
                    break;
                case 2:
                    pk = PreisKategorie.MAXIMALPREIS;
                    break;
                default:
                    break;
                }
            	query.setPreis(pk);
            	Calendar cal = Calendar.getInstance();
            	cal.set(Calendar.YEAR, dateTimeFrom.getYear());
            	cal.set(Calendar.MONTH, dateTimeFrom.getMonth());
            	cal.set(Calendar.DAY_OF_MONTH, dateTimeFrom.getDay());
            	query.setDatumuhrzeit(new Date(cal.getTimeInMillis()));
                cal.set(Calendar.YEAR, dateTimeTo.getYear());
                cal.set(Calendar.MONTH, dateTimeTo.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, dateTimeTo.getDay());
            	queryto.setDatumuhrzeit(new Date(cal.getTimeInMillis()));
                LOG.debug("Query Auffuehrung: {}", query);
            	
            	tableViewer.setInput(auffuehrungService.find(query, queryto));
            	tableViewer.refresh();
            	
            }
        });
        this.tableViewer.setInput(auffuehrungService.find(new Auffuehrung(), new Auffuehrung()));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		dateTimeFrom.setFocus();
	}
}
