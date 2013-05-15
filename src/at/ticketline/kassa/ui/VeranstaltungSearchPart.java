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

public class VeranstaltungSearchPart {

	private Text txtBezeichnung;
	private Text txtDauerVon;
	private Text txtDauerBis;
	private Text txtOrt;
	private Text txtInhalt;
	private Table table;

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
		fd_lblBezeichnung.right = new FormAttachment(txtBezeichnung, -6);
		FormData fd_txtBezeichnung = new FormData();
		fd_txtBezeichnung.right = new FormAttachment(0, 168);
		fd_txtBezeichnung.top = new FormAttachment(0, 21);
		fd_txtBezeichnung.left = new FormAttachment(0, 88);
		txtBezeichnung.setLayoutData(fd_txtBezeichnung);
		
		Label lblStrae = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblStrae = new FormData();
		fd_lblStrae.top = new FormAttachment(lblBezeichnung, 11);
		fd_lblStrae.left = new FormAttachment(lblBezeichnung, 0, SWT.LEFT);
		lblStrae.setLayoutData(fd_lblStrae);
		lblStrae.setText("Dauer (min)");
		
		txtDauerVon = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtDauerVon = new FormData();
		fd_txtDauerVon.right = new FormAttachment(txtBezeichnung, 0, SWT.RIGHT);
		fd_txtDauerVon.left = new FormAttachment(lblStrae, 11);
		fd_txtDauerVon.top = new FormAttachment(txtBezeichnung, 6);
		txtDauerVon.setLayoutData(fd_txtDauerVon);
		
		txtDauerBis = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtDauerBis = new FormData();
		fd_txtDauerBis.top = new FormAttachment(lblStrae, -3, SWT.TOP);
		txtDauerBis.setLayoutData(fd_txtDauerBis);
		
		Label lblOrtstyp = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblOrtstyp = new FormData();
		fd_lblOrtstyp.left = new FormAttachment(txtBezeichnung, 114);
		fd_lblOrtstyp.top = new FormAttachment(lblBezeichnung, 0, SWT.TOP);
		lblOrtstyp.setLayoutData(fd_lblOrtstyp);
		lblOrtstyp.setText("Kategorie");
		
		Label lblLand = new Label(SearchComposite, SWT.NONE);
		fd_txtDauerBis.right = new FormAttachment(lblLand, -11);
		FormData fd_lblLand = new FormData();
		fd_lblLand.top = new FormAttachment(lblOrtstyp, 12);
		fd_lblLand.left = new FormAttachment(lblOrtstyp, 0, SWT.LEFT);
		lblLand.setLayoutData(fd_lblLand);
		lblLand.setText("Inhalt");
		
		txtInhalt = new Text(SearchComposite, SWT.BORDER);
		FormData fd_txtInhalt = new FormData();
		fd_txtInhalt.right = new FormAttachment(lblLand, 147, SWT.RIGHT);
		fd_txtInhalt.left = new FormAttachment(lblLand, 30);
		fd_txtInhalt.top = new FormAttachment(lblStrae, -3, SWT.TOP);
		txtInhalt.setLayoutData(fd_txtInhalt);
		
		ComboViewer comboViewer = new ComboViewer(SearchComposite, SWT.NONE);
		Combo comboKategorie = comboViewer.getCombo();
		FormData fd_comboKategorie = new FormData();
		fd_comboKategorie.right = new FormAttachment(lblOrtstyp, 132, SWT.RIGHT);
		fd_comboKategorie.left = new FormAttachment(lblOrtstyp, 10);
		fd_comboKategorie.top = new FormAttachment(lblBezeichnung, -4, SWT.TOP);
		comboKategorie.setLayoutData(fd_comboKategorie);
		
		Label label = new Label(SearchComposite, SWT.NONE);
		fd_txtDauerBis.left = new FormAttachment(label, 6);
		label.setText("-");
		FormData fd_label = new FormData();
		fd_label.top = new FormAttachment(lblStrae, 0, SWT.TOP);
		fd_label.left = new FormAttachment(txtDauerVon, 6);
		label.setLayoutData(fd_label);
		SearchComposite.setTabList(new Control[]{txtBezeichnung, txtDauerVon, txtDauerBis, comboKategorie, txtInhalt, btnSuchen});
		
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
