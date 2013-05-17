package at.ticketline.kassa.ui;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.AuffuehrungService;
import at.ticketline.test.EntityGenerator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener; 

@SuppressWarnings("restriction")
public class VeranstaltungPart {
    private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungPart.class);
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    @Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    //@Inject private Kunde kuenstler;
    @Inject private AuffuehrungService auffuehrungService;
    
    private FormToolkit toolkit;
    private ScrolledForm form;
    private TableViewer tableViewer;

    @Inject
    public void init(Composite parent, @Named (IServiceConstants.ACTIVE_SELECTION)
    				 @Optional Veranstaltung veranstaltung) throws PartInitException {
		
        createControls(parent);
    }
    
    private void createControls(Composite parent){
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
        Section auffuehrungSection = this.toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
        auffuehrungSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                VeranstaltungPart.this.form.reflow(true);
            }
        });
        auffuehrungSection.setText("Aufführungen");
        auffuehrungSection.setLayoutData(new GridData(GridData.FILL_BOTH));
    
        this.tableViewer = new TableViewer(auffuehrungSection, SWT.BORDER | SWT.FULL_SELECTION);
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
                Auffuehrung a = (Auffuehrung) element;
                switch (index) {
                case 0:
                    if (a.getDatumuhrzeit() != null) {
                        return a.getDatumuhrzeit().toString();
                    } else {
                        return "";
                    }
                case 1:
                    if (a.getHinweis() != null) {
                        return a.getHinweis();
                    } else {
                        return "";
                    }
                case 2:
                    if (a.getSaal().getBezeichnung() != null) {
                        return a.getSaal().getBezeichnung();
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
        col1.setText("Datum");
        TableColumn col2 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col2.setText("Bezeichnung");
        TableColumn col3 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col3.setText("Saal");
        //TableColumn col4 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        //col4.setText("Gage");
        
        // MAGIC HAPPENS HERE
        this.tableViewer.setInput(this.auffuehrungService.find(new Auffuehrung()));
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
                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openKunde", null);
                handlerService.executeHandler(c);
            }
        });
    
        this.toolkit.adapt(this.tableViewer.getTable(), true, true);
        auffuehrungSection.setClient(this.tableViewer.getTable());
    }
}
