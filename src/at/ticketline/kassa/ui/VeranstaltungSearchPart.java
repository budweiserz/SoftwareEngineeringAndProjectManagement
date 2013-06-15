package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ArrayContentProvider;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.KategorieService;
import at.ticketline.service.api.VeranstaltungService;

@SuppressWarnings("restriction")
public class VeranstaltungSearchPart {

	private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungSearchPart.class);

	private Text txtBezeichnung;
	private Text txtDauerVon;
	private Text txtDauerBis;
	private Text txtKategorie;
	private Text txtInhalt;
	private Table table;

	private TableViewer tableViewer;

	@Inject
	private VeranstaltungService veranstaltungService;

	@Inject
	private KategorieService kategorieService;

	@Inject private ESelectionService selectionService;
	@Inject private EHandlerService handlerService;
	@Inject private ECommandService commandService;

	public VeranstaltungSearchPart() {
	}

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
		btnSuchen.setText("suchen");

		Label lblBezeichnung = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblBezeichnung = new FormData();
		fd_lblBezeichnung.left = new FormAttachment(0, 10);
		fd_lblBezeichnung.top = new FormAttachment(0, 24);
		lblBezeichnung.setLayoutData(fd_lblBezeichnung);
		lblBezeichnung.setText("Bezeichnung");

		txtBezeichnung = new Text(SearchComposite, SWT.BORDER);
		fd_lblBezeichnung.right = new FormAttachment(txtBezeichnung, -3);
		FormData fd_txtBezeichnung = new FormData();
		fd_txtBezeichnung.right = new FormAttachment(0, 178);
		fd_txtBezeichnung.left = new FormAttachment(0, 98);
		fd_txtBezeichnung.top = new FormAttachment(lblBezeichnung, -5, SWT.TOP);
		txtBezeichnung.setLayoutData(fd_txtBezeichnung);

		Label lblStrae = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblStrae = new FormData();
		fd_lblStrae.top = new FormAttachment(lblBezeichnung, 18);
		fd_lblStrae.left = new FormAttachment(0, 10);
		lblStrae.setLayoutData(fd_lblStrae);
		lblStrae.setText("Dauer (min)");

		txtDauerVon = new Text(SearchComposite, SWT.BORDER);
		txtDauerVon.setText("  ");
		FormData fd_txtDauerVon = new FormData();
		fd_txtDauerVon.right = new FormAttachment(lblStrae, 98, SWT.RIGHT);
		fd_txtDauerVon.left = new FormAttachment(lblStrae, 18);
		fd_txtDauerVon.top = new FormAttachment(lblBezeichnung, 13);
		txtDauerVon.setLayoutData(fd_txtDauerVon);

		txtDauerBis = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtDauerBis = new FormData();
		fd_txtDauerBis.top = new FormAttachment(0, 54);
		fd_txtDauerBis.bottom = new FormAttachment(100, -19);
		fd_txtDauerBis.right = new FormAttachment(0, 276);
		txtDauerBis.setLayoutData(fd_txtDauerBis);

		Label lblOrtstyp = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrtstyp = new FormData();
		lblOrtstyp.setLayoutData(fd_lblOrtstyp);
		lblOrtstyp.setText("Kategorie");

		Label lblLand = new Label(SearchComposite, SWT.NONE);
		fd_lblOrtstyp.bottom = new FormAttachment(lblLand, -18);
		FormData fd_lblLand = new FormData();
		fd_lblLand.left = new FormAttachment(lblOrtstyp, 0, SWT.LEFT);
		fd_lblLand.top = new FormAttachment(0, 59);
		lblLand.setLayoutData(fd_lblLand);
		lblLand.setText("Inhalt");

		txtInhalt = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtInhalt = new FormData();
		fd_txtInhalt.right = new FormAttachment(lblLand, 145, SWT.RIGHT);
		fd_txtInhalt.left = new FormAttachment(lblLand, 28);
		txtInhalt.setLayoutData(fd_txtInhalt);

		Label label = new Label(SearchComposite, SWT.NONE);
		fd_txtDauerBis.left = new FormAttachment(0, 196);
		label.setText("-");
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(txtDauerBis, -6);
		fd_label.bottom = new FormAttachment(100, -24);
		label.setLayoutData(fd_label);

		txtKategorie = new Text(SearchComposite, SWT.BORDER);
		fd_lblOrtstyp.right = new FormAttachment(txtKategorie, -7);
		fd_txtInhalt.top = new FormAttachment(0, 54);
		FormData fd_txtKategorie_1 = new FormData();
		fd_txtKategorie_1.bottom = new FormAttachment(0, 46);
		fd_txtKategorie_1.right = new FormAttachment(0, 475);
		fd_txtKategorie_1.top = new FormAttachment(0, 19);
		fd_txtKategorie_1.left = new FormAttachment(0, 358);
		txtKategorie.setLayoutData(fd_txtKategorie_1);
		SearchComposite.setTabList(new Control[]{txtBezeichnung, txtDauerVon, txtDauerBis, txtInhalt, btnSuchen});

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
		tblclmnStrasse.setText("Dauer");

		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnPlz = tableViewerColumn_2.getColumn();
		tblclmnPlz.setWidth(70);
		tblclmnPlz.setText("Kategorie");

		TableViewerColumn tableViewerColumn_3 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnOrt = tableViewerColumn_3.getColumn();
		tblclmnOrt.setWidth(100);
		tblclmnOrt.setText("Inhalt");

		tableViewer.setContentProvider(new ArrayContentProvider());

		tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int index) {
				Veranstaltung e = (Veranstaltung) element;
				switch (index) {
				case 0:
					if (e.getBezeichnung() != null) {
						return e.getBezeichnung();
					} else {
						return "";
					}
				case 1:
					if (e.getDauer() != null) {
						return e.getDauer().toString();
					} else {
						return "";
					}
				case 2:
					if (e.getKategorie() != null) {
						return e.getKategorie();
					} else {
						return "";
					}
				case 3:
					if (e.getInhalt() != null) {
						return e.getInhalt();
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
				ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openVeranstaltung", null);
				handlerService.executeHandler(c);
			}
		});


		btnSuchen.addMouseListener(new MouseListener() {
			@Override public void mouseDoubleClick(MouseEvent e) { }
			@Override public void mouseDown(MouseEvent e) { }

			@Override
			public void mouseUp(MouseEvent e) {
				Veranstaltung query = new Veranstaltung();
				query.setBezeichnung(txtBezeichnung.getText().length() > 0 ? txtBezeichnung.getText() : null);
				query.setKategorie(txtKategorie.getText().length() > 0 ? txtKategorie.getText() : null);
				query.setInhalt(txtInhalt.getText().length() > 0 ? txtInhalt.getText() : null);
				try {
					query.setDauer(txtDauerVon.getText().length() > 0 ? Integer.parseInt(txtDauerVon.getText()) : null);
				} catch (NumberFormatException ex) {
					LOG.error("Minimale Dauer keine gültige Eingabe {}", ex);
				}

				Integer minDauer = null;
				Integer maxDauer = null;

				try {
					minDauer = txtDauerVon.getText().length() > 0 ? Integer.parseInt(txtDauerVon.getText()) : null;
				} catch (NumberFormatException ex) {
					LOG.error("Minimale Dauer keine gültige Eingabe {}", ex);
				}
				
				try {
					maxDauer = txtDauerBis.getText().length() > 0 ? Integer.parseInt(txtDauerBis.getText()) : null;
				} catch (NumberFormatException ex) {
					LOG.error("Minimale Dauer keine gültige Eingabe {}", ex);
				}

				LOG.info("Query Veranstaltung: {} minDauer: {} maxDauer: {}", query);

				VeranstaltungSearchPart.this.tableViewer.setInput(veranstaltungService.find(query, minDauer, maxDauer));
				VeranstaltungSearchPart.this.tableViewer.refresh();
			}
		});


		this.tableViewer.setInput(veranstaltungService.find(new Veranstaltung(), null, null));
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtBezeichnung.setFocus();
	}

}
