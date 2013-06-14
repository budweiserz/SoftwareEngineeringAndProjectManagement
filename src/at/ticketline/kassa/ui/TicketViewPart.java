package at.ticketline.kassa.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Transaktion;
import at.ticketline.service.api.TransaktionService;

@SuppressWarnings("restriction")
public class TicketViewPart {
    private static final Logger LOG = LoggerFactory.getLogger(TicketViewPart.class);
    
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    //@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    @Inject private TransaktionService transaktionsService;
    
    TableViewer tableViewer;
	private Table table;
	private Text txtVorname;
	private Text txtNachname;
	private Text txtAuffuehrung;
	private Text txtBuchungsnr;
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
		
		Label lblVorname = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblVorname = new FormData();
		fd_lblVorname.top = new FormAttachment(0, 10);
		fd_lblVorname.left = new FormAttachment(0, 10);
		lblVorname.setLayoutData(fd_lblVorname);
		lblVorname.setText("Vorname");
		
		txtVorname = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtVorname = new FormData();
		fd_txtVorname.right = new FormAttachment(lblVorname, 148, SWT.RIGHT);
		fd_txtVorname.top = new FormAttachment(0, 5);
		fd_txtVorname.left = new FormAttachment(lblVorname, 38);
		txtVorname.setLayoutData(fd_txtVorname);
		
		Label lblNachname = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblNachname = new FormData();
		fd_lblNachname.top = new FormAttachment(lblVorname, 28);
		fd_lblNachname.left = new FormAttachment(lblVorname, 0, SWT.LEFT);
		lblNachname.setLayoutData(fd_lblNachname);
		lblNachname.setText("Nachname");
		
		txtNachname = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtNachname = new FormData();
		fd_txtNachname.right = new FormAttachment(txtVorname, 0, SWT.RIGHT);
		fd_txtNachname.top = new FormAttachment(lblNachname, -5, SWT.TOP);
		fd_txtNachname.left = new FormAttachment(txtVorname, 0, SWT.LEFT);
		txtNachname.setLayoutData(fd_txtNachname);
		
		Label lblAuffhrung = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblAuffhrung = new FormData();
		fd_lblAuffhrung.top = new FormAttachment(lblVorname, 0, SWT.TOP);
		fd_lblAuffhrung.left = new FormAttachment(txtVorname, 41);
		lblAuffhrung.setLayoutData(fd_lblAuffhrung);
		lblAuffhrung.setText("Aufführung");
		
		txtAuffuehrung = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtAuffuehrung = new FormData();
		fd_txtAuffuehrung.right = new FormAttachment(lblAuffhrung, 148, SWT.RIGHT);
		fd_txtAuffuehrung.top = new FormAttachment(0, 5);
		fd_txtAuffuehrung.left = new FormAttachment(lblAuffhrung, 38);
		txtAuffuehrung.setLayoutData(fd_txtAuffuehrung);
		
		Label lblBuchungsnr = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblBuchungsnr = new FormData();
		fd_lblBuchungsnr.bottom = new FormAttachment(lblNachname, 0, SWT.BOTTOM);
		fd_lblBuchungsnr.left = new FormAttachment(lblAuffhrung, 0, SWT.LEFT);
		lblBuchungsnr.setLayoutData(fd_lblBuchungsnr);
		lblBuchungsnr.setText("Nummer");
		
		txtBuchungsnr = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtBuchungsnr = new FormData();
		fd_txtBuchungsnr.right = new FormAttachment(txtAuffuehrung, 110);
		fd_txtBuchungsnr.top = new FormAttachment(lblBuchungsnr, -5, SWT.TOP);
		fd_txtBuchungsnr.left = new FormAttachment(txtAuffuehrung, 0, SWT.LEFT);
		txtBuchungsnr.setLayoutData(fd_txtBuchungsnr);
		
		SearchComposite.setTabList(new Control[]{btnSuchen});
		
		
		// TABLE
		
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn_0 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnBuchungsnr0 = tableViewerColumn_0.getColumn();
		tblclmnBuchungsnr0.setWidth(100);
		tblclmnBuchungsnr0.setText("Typ");

		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnBuchungsnr = tableViewerColumn_4.getColumn();
		tblclmnBuchungsnr.setWidth(100);
		tblclmnBuchungsnr.setText("Nummer");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnVorname = tableViewerColumn_5.getColumn();
		tblclmnVorname.setWidth(100);
		tblclmnVorname.setText("Vorname");
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnNachname = tableViewerColumn.getColumn();
		tblclmnNachname.setWidth(100);
		tblclmnNachname.setText("Nachname");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnAuffhrung = tableViewerColumn_1.getColumn();
		tblclmnAuffhrung.setWidth(100);
		tblclmnAuffhrung.setText("Aufführung");
		
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setLayout(new FormLayout());
		GridData gd_composite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
		gd_composite.minimumWidth = 600;
		gd_composite.minimumHeight = 48;
		gd_composite.widthHint = 1920;
		gd_composite.heightHint = 48;
		composite.setLayoutData(gd_composite);
		
		Button btnKaufen = new Button(composite, SWT.NONE);
		FormData fd_btnKaufen = new FormData();
		fd_btnKaufen.top = new FormAttachment(0, 10);
		fd_btnKaufen.left = new FormAttachment(0, 10);
		btnKaufen.setLayoutData(fd_btnKaufen);
		btnKaufen.setText("kaufen");
		
		Button btnBearbeiten = new Button(composite, SWT.NONE);
		FormData fd_btnBearbeiten = new FormData();
		fd_btnBearbeiten.bottom = new FormAttachment(btnKaufen, 0, SWT.BOTTOM);
		fd_btnBearbeiten.left = new FormAttachment(btnKaufen, 17);
		btnBearbeiten.setLayoutData(fd_btnBearbeiten);
		btnBearbeiten.setText("bearbeiten");
		
		Button btnStornieren = new Button(composite, SWT.NONE);
		FormData fd_btnStornieren = new FormData();
		fd_btnStornieren.top = new FormAttachment(btnKaufen, 0, SWT.TOP);
		fd_btnStornieren.left = new FormAttachment(btnBearbeiten, 19);
		btnStornieren.setLayoutData(fd_btnStornieren);
		btnStornieren.setText("stornieren");
		
        tableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                // The inputElement comes from view.setInput()
                if (inputElement instanceof List) {
                    List<?> models = (List<?>)inputElement;
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
                Transaktion e = (Transaktion) element;
                switch (index) {
                case 1:
                    if (e.getReservierungsnr() != null) {
                        return String.valueOf(e.getReservierungsnr());
                    } else {
                        return "";
                    }
                case 0:
                    if (e.getStatus() != null) {
                        return e.getStatus().toString();
                    } else {
                        return "";
                    }
                case 2:
                    if (e.getKunde() != null) {
                        return e.getKunde().getVorname();
                    } else {
                        return "";
                    }
                case 3:
                    if (e.getKunde() != null) {
                        return e.getKunde().getNachname();
                    } else {
                        return "";
                    }
                case 4:
                    if (e.getPlaetze() != null && e.getPlaetze().iterator().hasNext()) {
                        return String.valueOf(e.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung().getBezeichnung());
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
        
        /*btnSuchen.addMouseListener(new MouseListener() {
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
        */
        //MAGIC HAPPENS HERE
        this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection(); 
                selectionService.setSelection(selection.getFirstElement());
                LOG.info("Selection changed: {}", selection.getFirstElement().toString());
            }
        });
        this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openTicketWizard", null);
                handlerService.executeHandler(c);
            }
        });
        
        
        btnKaufen.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
        
        this.tableViewer.setInput(transaktionsService.find(new Transaktion()));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtBuchungsnr.setFocus();
	}
}
