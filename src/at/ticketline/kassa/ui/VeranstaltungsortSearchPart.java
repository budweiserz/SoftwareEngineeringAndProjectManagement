package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
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

public class VeranstaltungsortSearchPart {
	private Text txtBezeichnung;
	private Text txtStrasse;
	private Text txtPlz;
	private Text txtOrt;
	private Text txtLand;
	private Table table;

	public VeranstaltungsortSearchPart() {
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
		lblBezeichnung.setLayoutData(fd_lblBezeichnung);
		lblBezeichnung.setText("Bezeichnung");
		
		txtBezeichnung = new Text(SearchComposite, SWT.BORDER);
		fd_lblBezeichnung.top = new FormAttachment(txtBezeichnung, 3, SWT.TOP);
		fd_lblBezeichnung.right = new FormAttachment(txtBezeichnung, -6);
		FormData fd_txtBezeichnung = new FormData();
		fd_txtBezeichnung.top = new FormAttachment(0, 4);
		fd_txtBezeichnung.left = new FormAttachment(0, 96);
		txtBezeichnung.setLayoutData(fd_txtBezeichnung);
		
		Label lblStrae = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblStrae = new FormData();
		fd_lblStrae.top = new FormAttachment(lblBezeichnung, 12);
		fd_lblStrae.left = new FormAttachment(0, 10);
		lblStrae.setLayoutData(fd_lblStrae);
		lblStrae.setText("Straße");
		
		Label lblOrt = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrt = new FormData();
		fd_lblOrt.top = new FormAttachment(txtBezeichnung, 0, SWT.TOP);
		fd_lblOrt.left = new FormAttachment(txtBezeichnung, 130);
		lblOrt.setLayoutData(fd_lblOrt);
		lblOrt.setText("Ort");
		
		txtStrasse = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtStrasse = new FormData();
		fd_txtStrasse.right = new FormAttachment(txtBezeichnung, 0, SWT.RIGHT);
		fd_txtStrasse.top = new FormAttachment(txtBezeichnung, 6);
		fd_txtStrasse.left = new FormAttachment(txtBezeichnung, 0, SWT.LEFT);
		txtStrasse.setLayoutData(fd_txtStrasse);
		
		txtPlz = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtPlz = new FormData();
		fd_txtPlz.right = new FormAttachment(txtBezeichnung, 0, SWT.RIGHT);
		fd_txtPlz.top = new FormAttachment(txtStrasse, 5);
		fd_txtPlz.left = new FormAttachment(txtBezeichnung, 0, SWT.LEFT);
		txtPlz.setLayoutData(fd_txtPlz);
		
		Label lblPlz = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblPlz = new FormData();
		fd_lblPlz.top = new FormAttachment(lblStrae, 11);
		fd_lblPlz.left = new FormAttachment(lblBezeichnung, 0, SWT.LEFT);
		lblPlz.setLayoutData(fd_lblPlz);
		lblPlz.setText("PLZ");
		
		Label lblOrtstyp = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrtstyp = new FormData();
		fd_lblOrtstyp.bottom = new FormAttachment(lblStrae, 0, SWT.BOTTOM);
		fd_lblOrtstyp.left = new FormAttachment(lblOrt, 0, SWT.LEFT);
		lblOrtstyp.setLayoutData(fd_lblOrtstyp);
		lblOrtstyp.setText("Ortstyp");
		
		Label lblLand = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblLand = new FormData();
		fd_lblLand.top = new FormAttachment(lblOrtstyp, 14);
		fd_lblLand.left = new FormAttachment(lblOrt, 0, SWT.LEFT);
		lblLand.setLayoutData(fd_lblLand);
		lblLand.setText("Land");
		
		txtOrt = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtOrt = new FormData();
		fd_txtOrt.right = new FormAttachment(lblOrt, 163, SWT.RIGHT);
		fd_txtOrt.top = new FormAttachment(0, 4);
		fd_txtOrt.left = new FormAttachment(lblOrt, 48);
		txtOrt.setLayoutData(fd_txtOrt);
		
		txtLand = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtLand = new FormData();
		fd_txtLand.right = new FormAttachment(lblLand, 152, SWT.RIGHT);
		fd_txtLand.left = new FormAttachment(lblLand, 37);
		fd_txtLand.top = new FormAttachment(txtPlz, 0, SWT.TOP);
		txtLand.setLayoutData(fd_txtLand);
		
		ComboViewer comboViewer = new ComboViewer(SearchComposite, SWT.NONE);
		Combo combo = comboViewer.getCombo();
		FormData fd_combo = new FormData();
		fd_combo.right = new FormAttachment(lblOrtstyp, 136, SWT.RIGHT);
		fd_combo.left = new FormAttachment(lblOrtstyp, 21);
		fd_combo.top = new FormAttachment(lblStrae, -3, SWT.TOP);
		combo.setLayoutData(fd_combo);
		SearchComposite.setTabList(new Control[]{txtBezeichnung, txtStrasse, txtPlz, txtOrt, combo, txtLand, btnSuchen});
		
		TableViewer tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
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
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		txtBezeichnung.setFocus();
	}
}
