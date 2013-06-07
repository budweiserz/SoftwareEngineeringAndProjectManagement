package at.ticketline.kassa.ui;

import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Kunde;
import at.ticketline.service.api.KundeService;
@SuppressWarnings("restriction")
public class TransaktionWizardSeiteVier extends WizardPage {

    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteVier.class);
    
    private ESelectionService selectionService;
    private KundeService kundeService;
    
    private TransaktionWizardValues values;
    
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableViewer tableViewer;
    
    private Button btnSuchen;
    private Text txtVorname;
    private Text txtNachname;
    /**
     * Diese Wizard-seite wird aufgerufen, wenn ein bestehender
     * Kunde für die Transaktion verwendet werden soll. Dabei wird die Liste
     * aus ListKundePart im Wizard-fenster angezeigt.
     */
    public TransaktionWizardSeiteVier(TransaktionWizardValues values) {
        super("BestehenderKunde");
        setTitle("Stammkunde");
        setDescription("Wählen sie hier den Kunden aus:");
        this.values = values;
    }
    

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        
        //TODO Delete?? old window
 //       ListKundePart lkp = new ListKundePart();
//        try {
//            lkp.init(container);
//        } catch (PartInitException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        
        setControl(container);
        
        createSuchfunktion(container);
        
        createKundenliste(container);
        
        createSuchlistener();

        //TODO set completed when customer type is selected

        
        setPageComplete(false);
        LOG.info("Wizard Seite zur Kundenanzeige erstellt!");
    }
    
    private void createSuchlistener() {
        btnSuchen.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }

            @Override
            public void mouseUp(MouseEvent e) {
                Kunde query = new Kunde();
                query.setVorname(txtVorname.getText().length() > 0 ? txtVorname.getText() : null);
                query.setNachname(txtNachname.getText().length() > 0 ? txtNachname.getText() : null);
                
                tableViewer.setInput(kundeService.findByKunde(query));
                
                LOG.debug("Query Ort: {}", query);
                
                tableViewer.refresh();
            }
        });
        
    }


    private void createSuchfunktion(Composite parent) {
        Composite SearchComposite = new Composite(parent, SWT.BORDER);
        SearchComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        SearchComposite.setLayout(new FormLayout());
        GridData gd_SearchComposite = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_SearchComposite.heightHint = 100;
        gd_SearchComposite.widthHint = 1920;
        gd_SearchComposite.minimumHeight = 100;
        gd_SearchComposite.minimumWidth = 600;
        SearchComposite.setLayoutData(gd_SearchComposite);
        
        Label lblVorname = new Label(SearchComposite, SWT.NONE);
        FormData fd_lblVorname = new FormData();
        fd_lblVorname.top = new FormAttachment(0, 10);
        fd_lblVorname.left = new FormAttachment(0, 10);
        lblVorname.setLayoutData(fd_lblVorname);
        lblVorname.setText("Vorname");
        
        Label lblNachname = new Label(SearchComposite, SWT.NONE);
        FormData fd_lblNachname = new FormData();
        fd_lblNachname.top = new FormAttachment(lblVorname, 20);
        fd_lblNachname.left = new FormAttachment(0, 10);
        lblNachname.setLayoutData(fd_lblNachname);
        lblNachname.setText("Nachname");
        
        txtVorname = new Text(SearchComposite, SWT.BORDER);
        FormData fd_txtVorname = new FormData();
        fd_txtVorname.top = new FormAttachment(lblVorname, -5, SWT.TOP);
        fd_txtVorname.right = new FormAttachment(lblVorname, 135, SWT.RIGHT);
        fd_txtVorname.left = new FormAttachment(lblVorname, 35);
        txtVorname.setLayoutData(fd_txtVorname);
        
        txtNachname = new Text(SearchComposite, SWT.BORDER);
        FormData fd_txtNachname = new FormData();
        fd_txtNachname.right = new FormAttachment(txtVorname, 0, SWT.RIGHT);
        fd_txtNachname.top = new FormAttachment(lblNachname, -5, SWT.TOP);
        fd_txtNachname.left = new FormAttachment(txtVorname, 0, SWT.LEFT);
        txtNachname.setLayoutData(fd_txtNachname);
        
        btnSuchen = new Button(SearchComposite, SWT.NONE);
        FormData fd_btnSuchen = new FormData();
        fd_btnSuchen.bottom = new FormAttachment(100, -10);
        fd_btnSuchen.right = new FormAttachment(100, -10);
        btnSuchen.setLayoutData(fd_btnSuchen);
        btnSuchen.setText("Suchen");
    }
    
    private void createKundenliste(Composite parent){
        parent.setLayout(new GridLayout(1, false));

        this.toolkit = new FormToolkit(parent.getDisplay());
        this.form = this.toolkit.createScrolledForm(parent);
        this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.form.getBody().setLayout(new GridLayout(1, false));

        this.createForm(this.form.getBody());
        this.createTable(this.form.getBody());
        //this.createSaveButton(this.form.getBody());
    }
    
    private void createForm(Composite parent) {
        Composite c = this.toolkit.createComposite(parent);
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ColumnLayout columnLayout = new ColumnLayout();
        columnLayout.minNumColumns = 1;
        columnLayout.maxNumColumns = 1;
        c.setLayout(columnLayout);
    }

    private void createTable(Composite parent) {
        this.tableViewer = new TableViewer(parent);
        this.tableViewer.getTable().setLayoutData( new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        //layout.addColumnData(new ColumnWeightData(15, 100, true));
        this.tableViewer.getTable().setLayout(layout);
    
        this.tableViewer.getTable().setLinesVisible(true);
        this.tableViewer.getTable().setHeaderVisible(true);
    
        this.tableViewer.setContentProvider(new ArrayContentProvider());
        this.tableViewer.setLabelProvider(new ITableLabelProvider() {
            @Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
            @Override
            public String getColumnText(Object element, int index) {
                Kunde e = (Kunde) element;
                switch (index) {
                case 0:
                    if (e.getVorname() != null) {
                        return e.getVorname();
                    } else {
                        return "";
                    }
                case 1:
                    if (e.getNachname() != null) {
                        return e.getNachname();
                    } else {
                        return "";
                    }
                case 2:
                    if (e.getGeburtsdatum() != null) {
                        return e.getGeburtsdatum().getTime().toString();
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
    
        TableColumn col1 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col1.setText("Vorname");
        TableColumn col2 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col2.setText("Nachname");
        TableColumn col3 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col3.setText("Geburtsdatum");
        
        // MAGIC HAPPENS HERE
        this.tableViewer.setInput(this.kundeService.findAll());
        final TransaktionWizardSeiteVier temp = this;
        this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                temp.setPageComplete(true);
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection(); 
                selectionService.setSelection(selection.getFirstElement());
                temp.values.setKunde((Kunde)selection.getFirstElement());
                ((TransaktionWizard)getWizard()).fuenf.updateContent();
                LOG.info("Type: " + selection.getFirstElement().getClass().getName());
                LOG.info("Selection changed: {}", selection.getFirstElement().toString());
            }
        });
    
        this.toolkit.adapt(this.tableViewer.getTable(), true, true);

    }
    
    public void setKundeService(KundeService kundeService) {
        this.kundeService = kundeService;
    }
    
    public void setESelectionService( ESelectionService selectionService) {
        this.selectionService = selectionService;
    }
    
    @Override
    public WizardPage getNextPage(){
        WizardPage fuenf = ((TransaktionWizard)getWizard()).fuenf;
        return fuenf;
     }
    
}