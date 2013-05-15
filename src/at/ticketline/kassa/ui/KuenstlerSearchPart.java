package at.ticketline.kassa.ui;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IDoubleClickListener; 
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Control;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Adresse;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Geschlecht;
import at.ticketline.service.api.KuenstlerService;
import at.ticketline.service.api.OrtService;

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
    
	private Text txtVorname;
	private Text txtNachname;
	private Combo combo;
	
	private Table table;

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
		gd_SearchComposite.heightHint = 79;
		gd_SearchComposite.widthHint = 1920;
		gd_SearchComposite.minimumHeight = 100;
		gd_SearchComposite.minimumWidth = 600;
		SearchComposite.setLayoutData(gd_SearchComposite);
		
		Button btnSuchen = new Button(SearchComposite, SWT.NONE);
		FormData fd_btnSuchen = new FormData();
		fd_btnSuchen.bottom = new FormAttachment(100, -10);
		fd_btnSuchen.right = new FormAttachment(100, -10);
		fd_btnSuchen.left = new FormAttachment(100, -67);
		btnSuchen.setLayoutData(fd_btnSuchen);
		btnSuchen.setText("suchen");
		
		Label lblBezeichnung = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblBezeichnung = new FormData();
		fd_lblBezeichnung.right = new FormAttachment(0, 90);
		fd_lblBezeichnung.left = new FormAttachment(0, 38);
		fd_lblBezeichnung.top = new FormAttachment(0, 13);
		lblBezeichnung.setLayoutData(fd_lblBezeichnung);
		lblBezeichnung.setText("Vorname");
		
		txtVorname = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtVorname = new FormData();
		fd_txtVorname.top = new FormAttachment(lblBezeichnung, -3, SWT.TOP);
		fd_txtVorname.left = new FormAttachment(lblBezeichnung, 6);
		fd_txtVorname.right = new FormAttachment(100, -415);
		txtVorname.setLayoutData(fd_txtVorname);
		
		Label lblOrt = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrt = new FormData();
		fd_lblOrt.top = new FormAttachment(lblBezeichnung, 0, SWT.TOP);
		fd_lblOrt.left = new FormAttachment(txtVorname, 32);
		lblOrt.setLayoutData(fd_lblOrt);
		lblOrt.setText("Nachname");
		
		Label lblOrtstyp = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrtstyp = new FormData();
		lblOrtstyp.setLayoutData(fd_lblOrtstyp);
		lblOrtstyp.setText("Geschlecht");
		
		txtNachname = new Text(SearchComposite, SWT.BORDER);
		fd_lblOrt.right = new FormAttachment(100, -323);
		FormData fd_txtNachname = new FormData();
		fd_txtNachname.top = new FormAttachment(lblBezeichnung, -3, SWT.TOP);
		fd_txtNachname.left = new FormAttachment(lblOrt, 21);
		fd_txtNachname.right = new FormAttachment(100, -187);
		txtNachname.setLayoutData(fd_txtNachname);
		
		ComboViewer comboViewer = new ComboViewer(SearchComposite, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		combo.add("");
		combo.add(Geschlecht.MAENNLICH.toString());
		combo.add(Geschlecht.WEIBLICH.toString());
		
		fd_lblOrtstyp.top = new FormAttachment(combo, 4, SWT.TOP);
		fd_lblOrtstyp.right = new FormAttachment(combo, -6);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(txtVorname, 6);
		fd_combo.right = new FormAttachment(btnSuchen, -341);
		fd_combo.left = new FormAttachment(0, 96);
		combo.setLayoutData(fd_combo);
		SearchComposite.setTabList(new Control[]{txtVorname, txtNachname, combo, btnSuchen});
		
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
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtVorname.setFocus();
	}
}
