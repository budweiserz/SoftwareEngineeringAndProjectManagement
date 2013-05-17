package at.ticketline.kassa.ui;

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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.service.api.KuenstlerService;

@SuppressWarnings("restriction")
public class KuenstlerSearchPart {
    private static final Logger LOG = LoggerFactory.getLogger(KuenstlerSearchPart.class);
    
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    //@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    //@Inject private Ort ort;
    @Inject private KuenstlerService kuenstlerService;
    
    private TableViewer tableViewer;
	
    private Button btnSuchen;
	private Table table;
	private Text txtVorname;
	private Text txtNachname;
	private Combo combo;

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
		
		Label lblGeschlecht = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblGeschlecht = new FormData();
		fd_lblGeschlecht.top = new FormAttachment(lblVorname, 0, SWT.TOP);
		fd_lblGeschlecht.left = new FormAttachment(txtVorname, 50);
		lblGeschlecht.setLayoutData(fd_lblGeschlecht);
		lblGeschlecht.setText("Geschlecht");
		
		ComboViewer comboViewer = new ComboViewer(SearchComposite, SWT.NONE);
		combo = comboViewer.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(0, 5);
		fd_combo.left = new FormAttachment(lblGeschlecht, 20);
		combo.setLayoutData(fd_combo);
		combo.add("");
		combo.add(Geschlecht.MAENNLICH.toString());
		combo.add(Geschlecht.WEIBLICH.toString());
		
		btnSuchen = new Button(SearchComposite, SWT.NONE);
		FormData fd_btnSuchen = new FormData();
		fd_btnSuchen.bottom = new FormAttachment(100, -10);
		fd_btnSuchen.right = new FormAttachment(100, -10);
		btnSuchen.setLayoutData(fd_btnSuchen);
		btnSuchen.setText("Suchen");
		
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnVorname = tableViewerColumn.getColumn();
		tblclmnVorname.setWidth(110);
		tblclmnVorname.setText("Vorname");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnNachname = tableViewerColumn_1.getColumn();
		tblclmnNachname.setWidth(150);
		tblclmnNachname.setText("Nachname");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnSex = tableViewerColumn_2.getColumn();
		tblclmnSex.setWidth(100);
		tblclmnSex.setText("Geschlecht");
		
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new ITableLabelProvider() {

            @Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
            @Override
            public String getColumnText(Object element, int index) {
                Kuenstler e = (Kuenstler) element;
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
                    if (e.getGeschlecht().toString() != null) {
                        return e.getGeschlecht().toString();
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
                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openKuenstler", null);
                handlerService.executeHandler(c);
            }
        });
        
        btnSuchen.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }

            @Override
            public void mouseUp(MouseEvent e) {
                Kuenstler query = new Kuenstler();
            	query.setVorname(txtVorname.getText().length() > 0 ? txtVorname.getText() : null);
            	query.setNachname(txtNachname.getText().length() > 0 ? txtNachname.getText() : null);
            	
            	LOG.debug("Selectionindex: {}", combo.getSelectionIndex());
            	
            	if(combo.getSelectionIndex() <= 0) { // <= for empty selection
                	tableViewer.setInput(kuenstlerService.findByKuenstlerWithAnySex(query));
            	} else {
            	    query.setGeschlecht(Geschlecht.values()[combo.getSelectionIndex() - 1]); // -1 for empty selection
                	tableViewer.setInput(kuenstlerService.findByKuenstler(query));
            	}
            	
              	LOG.debug("Query Ort: {}", query);
              	
                tableViewer.refresh();
            }
        });
        
        tableViewer.setInput(kuenstlerService.findByKuenstlerWithAnySex(new Kuenstler()));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtVorname.setFocus();
	}
}
