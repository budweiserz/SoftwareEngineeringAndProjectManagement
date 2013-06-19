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
import org.eclipse.jface.viewers.ComboViewer;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Adresse;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.kassa.ui.sorter.OrtColumnViewerSorter;
import at.ticketline.service.api.OrtService;

@SuppressWarnings("restriction")
public class VeranstaltungsortSearchPart {
    private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungsortSearchPart.class);
    
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    //@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    //@Inject private Ort ort;
    @Inject private OrtService ortService;
    
    TableViewer tableViewer;
    
	private Text txtBezeichnung;
	private Text txtStrasse;
	private Text txtPlz;
	private Text txtOrt;
	private Text txtLand;
	private Table table;
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
		
		Button btnSuchen = new Button(SearchComposite, SWT.NONE);
		FormData fd_btnSuchen = new FormData();
		fd_btnSuchen.left = new FormAttachment(100, -67);
		fd_btnSuchen.bottom = new FormAttachment(100, -10);
		fd_btnSuchen.right = new FormAttachment(100, -10);
		btnSuchen.setLayoutData(fd_btnSuchen);
		btnSuchen.setText("Suchen");
		
		Label lblBezeichnung = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblBezeichnung = new FormData();
		fd_lblBezeichnung.top = new FormAttachment(0, 7);
		lblBezeichnung.setLayoutData(fd_lblBezeichnung);
		lblBezeichnung.setText("Bezeichnung");
		
		txtBezeichnung = new Text(SearchComposite, SWT.BORDER);
		fd_lblBezeichnung.right = new FormAttachment(txtBezeichnung, -6);
		FormData fd_txtBezeichnung = new FormData();
		fd_txtBezeichnung.left = new FormAttachment(0, 96);
		fd_txtBezeichnung.top = new FormAttachment(0, 4);
		txtBezeichnung.setLayoutData(fd_txtBezeichnung);
		
		Label lblStrae = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblStrae = new FormData();
		fd_lblStrae.left = new FormAttachment(0, 14);
		lblStrae.setLayoutData(fd_lblStrae);
		lblStrae.setText("Straße");
		
		Label lblOrt = new Label(SearchComposite, SWT.NONE);
		fd_txtBezeichnung.right = new FormAttachment(lblOrt, -79);
		FormData fd_lblOrt = new FormData();
		fd_lblOrt.top = new FormAttachment(lblBezeichnung, 0, SWT.TOP);
		fd_lblOrt.left = new FormAttachment(0, 290);
		lblOrt.setLayoutData(fd_lblOrt);
		lblOrt.setText("Ort");
		
		txtStrasse = new Text(SearchComposite, SWT.BORDER);
		fd_lblStrae.top = new FormAttachment(txtStrasse, 5, SWT.TOP);
		FormData fd_txtStrasse = new FormData();
		fd_txtStrasse.left = new FormAttachment(lblStrae, 43);
		fd_txtStrasse.top = new FormAttachment(txtBezeichnung, 6);
		txtStrasse.setLayoutData(fd_txtStrasse);
		
		txtPlz = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtPlz = new FormData();
		fd_txtPlz.top = new FormAttachment(txtStrasse, 5);
		txtPlz.setLayoutData(fd_txtPlz);
		
		Label lblPlz = new Label(SearchComposite, SWT.NONE);
		fd_txtPlz.left = new FormAttachment(lblPlz, 60);
		FormData fd_lblPlz = new FormData();
		fd_lblPlz.top = new FormAttachment(txtPlz, 5, SWT.TOP);
		fd_lblPlz.left = new FormAttachment(0, 14);
		lblPlz.setLayoutData(fd_lblPlz);
		lblPlz.setText("PLZ");
		
		Label lblOrtstyp = new Label(SearchComposite, SWT.NONE);
		fd_txtStrasse.right = new FormAttachment(lblOrtstyp, -79);
		FormData fd_lblOrtstyp = new FormData();
		fd_lblOrtstyp.top = new FormAttachment(lblStrae, 0, SWT.TOP);
		fd_lblOrtstyp.left = new FormAttachment(0, 290);
		lblOrtstyp.setLayoutData(fd_lblOrtstyp);
		lblOrtstyp.setText("Ortstyp");
		
		Label lblLand = new Label(SearchComposite, SWT.NONE);
		fd_txtPlz.right = new FormAttachment(lblLand, -79);
		FormData fd_lblLand = new FormData();
		fd_lblLand.top = new FormAttachment(lblPlz, 0, SWT.TOP);
		fd_lblLand.left = new FormAttachment(0, 290);
		lblLand.setLayoutData(fd_lblLand);
		lblLand.setText("Land");
		
		txtOrt = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtOrt = new FormData();
		fd_txtOrt.right = new FormAttachment(lblOrt, 162, SWT.RIGHT);
		fd_txtOrt.left = new FormAttachment(lblOrt, 47);
		fd_txtOrt.top = new FormAttachment(txtBezeichnung, 0, SWT.TOP);
		txtOrt.setLayoutData(fd_txtOrt);
		
		txtLand = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtLand = new FormData();
		fd_txtLand.right = new FormAttachment(lblLand, 152, SWT.RIGHT);
		fd_txtLand.left = new FormAttachment(lblLand, 37);
		txtLand.setLayoutData(fd_txtLand);
		
		ComboViewer comboViewer = new ComboViewer(SearchComposite, SWT.READ_ONLY);
		combo = comboViewer.getCombo();
		fd_txtLand.top = new FormAttachment(combo, 6);
		combo.add("");
		for (int i=0; i < Ortstyp.values().length; i++) {
			combo.add(Ortstyp.values()[i].toString());
		}
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(lblOrtstyp, 136, SWT.RIGHT);
		fd_combo.top = new FormAttachment(txtOrt, 7);
		fd_combo.left = new FormAttachment(lblOrtstyp, 19);
		combo.setLayoutData(fd_combo);
		SearchComposite.setTabList(new Control[]{txtBezeichnung, txtStrasse, txtPlz, txtOrt, combo, txtLand, btnSuchen});
		
		tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnBezeichnung = tableViewerColumn.getColumn();
		tblclmnBezeichnung.setWidth(110);
		tblclmnBezeichnung.setText("Bezeichnung");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnStrasse = tableViewerColumn_1.getColumn();
		tblclmnStrasse.setWidth(150);
		tblclmnStrasse.setText("Straße");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnPlz = tableViewerColumn_2.getColumn();
		tblclmnPlz.setWidth(70);
		tblclmnPlz.setText("PLZ");
		
		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnOrt = tableViewerColumn_3.getColumn();
		tblclmnOrt.setWidth(100);
		tblclmnOrt.setText("Ort");
		
		TableViewerColumn tableViewerColumn_4 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnOrtstyp = tableViewerColumn_4.getColumn();
		tblclmnOrtstyp.setWidth(100);
		tblclmnOrtstyp.setText("Ortstyp");
		
		TableViewerColumn tableViewerColumn_5 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnLand = tableViewerColumn_5.getColumn();
		tblclmnLand.setWidth(150);
		tblclmnLand.setText("Land");
		
		// add sort feature to table
		tableViewer.setSorter(new OrtColumnViewerSorter());
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn, OrtColumnViewerSorter.BEZEICHNUNG);
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn_1, OrtColumnViewerSorter.STRASSE);
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn_2, OrtColumnViewerSorter.PLZ);
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn_3, OrtColumnViewerSorter.ORT);
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn_4, OrtColumnViewerSorter.ORTSTYP);
		UIUtilities.addTableViewerColumnSorter(tableViewer, tableViewerColumn_5, OrtColumnViewerSorter.LAND);
		
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
                Ort e = (Ort) element;
                switch (index) {
                case 0:
                    if (e.getBezeichnung() != null) {
                        return e.getBezeichnung();
                    } else {
                        return "";
                    }
                case 1:
                    if (e.getAdresse().getStrasse() != null) {
                        return e.getAdresse().getStrasse();
                    } else {
                        return "";
                    }
                case 2:
                    if (e.getAdresse().getPlz() != null) {
                        return e.getAdresse().getPlz();
                    } else {
                        return "";
                    }
                case 3:
                    if (e.getAdresse().getOrt() != null) {
                        return e.getAdresse().getOrt();
                    } else {
                        return "";
                    }
                case 4:
                    if (e.getOrtstyp() != null) {
                        return e.getOrtstyp().toString();
                    } else {
                        return "";
                    }
                case 5:
                    if (e.getAdresse().getLand() != null) {
                        return e.getAdresse().getLand();
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
                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openVeranstaltungsort", null);
                handlerService.executeHandler(c);
            }
        });
        
        
        btnSuchen.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }

            @Override
            public void mouseUp(MouseEvent e) {
                Ort query = new Ort();
            	query.setBezeichnung(txtBezeichnung.getText().length() > 0 ? txtBezeichnung.getText() : null);
            	query.setAdresse(new Adresse());
            	query.getAdresse().setStrasse(txtStrasse.getText().length() > 0 ? txtStrasse.getText() : null);
            	query.getAdresse().setPlz(txtPlz.getText().length() > 0 ? txtPlz.getText() : null);
            	query.getAdresse().setOrt(txtOrt.getText().length() > 0 ? txtOrt.getText() : null);
            	query.getAdresse().setLand(txtLand.getText().length() > 0 ? txtLand.getText() : null);
            	query.setOrtstyp(combo.getSelectionIndex() > 0 ? Ortstyp.values()[combo.getSelectionIndex() - 1] : null);
            	
            	LOG.debug("Query Ort: {}", query);
            	
            	tableViewer.setInput(ortService.findByOrt(query));
            	tableViewer.refresh();
            }
        });
        
        tableViewer.setInput(ortService.findByOrt(new Ort()));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtBezeichnung.setFocus();
	}
}
