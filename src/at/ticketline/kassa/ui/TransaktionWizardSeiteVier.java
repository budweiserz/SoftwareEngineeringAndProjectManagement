package at.ticketline.kassa.ui;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.internal.registry.osgi.Activator;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Kunde;
import at.ticketline.service.api.KundeService;
@SuppressWarnings("restriction")
public class TransaktionWizardSeiteVier extends WizardPage {

    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteZwei.class);
    
    private EHandlerService handlerService;
    private ECommandService commandService;
    private ESelectionService selectionService;
    @Inject private KundeService kundeService;
    
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableViewer tableViewer;
    /**
     * Diese Wizard-seite wird aufgerufen, wenn ein bestehender
     * Kunde für die Transaktion verwendet werden soll. Dabei wird die Liste
     * aus ListKundePart im Wizard-fenster angezeigt.
     */
    public TransaktionWizardSeiteVier() {
        super("BestehenderKunde");
        setTitle("Stammkunde");
        setDescription("Wählen sie hier den Kunden aus:");
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
        
        
        createKundenliste(container);

        //TODO set completed when customer type is selected

        
        setPageComplete(false);
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
        //TODO delete, aufklappbares Teil:
//        Section engagementSection = this.toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
//        engagementSection.addExpansionListener(new ExpansionAdapter() {
//            @Override
//            public void expansionStateChanged(ExpansionEvent e) {
//                ListKundePart.this.form.reflow(true);
//            }
//        });
//        engagementSection.setText("Kunden");
//        engagementSection.setLayoutData(new GridData(GridData.FILL_BOTH));
//    
//        this.tableViewer = new TableViewer(engagementSection, SWT.BORDER | SWT.FULL_SELECTION);
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
        //TableColumn col4 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        //col4.setText("Gage");
        
        // MAGIC HAPPENS HERE
        this.tableViewer.setInput(this.kundeService.findAll());
        final TransaktionWizardSeiteVier temp = this;
        this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                temp.setPageComplete(true);
                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection(); 
                selectionService.setSelection(selection.getFirstElement());
                LOG.info("Selection changed: {}", selection.getFirstElement().toString());
            }
        });
//        this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
//            @Override
//            public void doubleClick(DoubleClickEvent event) {
//                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openKunde", null);
//                handlerService.executeHandler(c);
//            }
//        });
    
        this.toolkit.adapt(this.tableViewer.getTable(), true, true);
        //TODO delete legacy aufklapper
        //engagementSection.setClient(this.tableViewer.getTable());
    }
    
    public void setKundeService(KundeService kundeService) {
        this.kundeService = kundeService;
    }
    
    public void setEHandlerService(EHandlerService handlerService) {
        this.handlerService = handlerService;
    }
    
    public void setECommandService(ECommandService commandService) {
        this.commandService = commandService;
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