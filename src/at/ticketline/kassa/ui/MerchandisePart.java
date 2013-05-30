package at.ticketline.kassa.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.service.impl.ArtikelServiceImpl;
import org.eclipse.swt.widgets.Table;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Text;

public class MerchandisePart {

	private FormToolkit toolkit;
	private TableViewer merchandiseList;
	private ArtikelServiceImpl artikelService;
	private Table auswahl;
	
	private static final Device device = Display.getCurrent();
	private static final Color RED = new Color(device, 170, 34, 34);
	private static final Color WHITE = new Color(device, 255, 255, 255);
	private Text text;

	public MerchandisePart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.toolkit = new FormToolkit(parent.getDisplay());
		
		this.createTable(parent);
		
		createMiddleComposite(parent);
		
		this.creatAuswahl(parent);
		
	}

	private void createMiddleComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		toolkit.adapt(composite);
		toolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		{
			Composite header = new Composite(composite, SWT.NONE);
			header.setLayout(new GridLayout(2, false));
			GridData gd_header = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
			gd_header.widthHint = 140;
			gd_header.heightHint = 45;
			header.setLayoutData(gd_header);
			toolkit.adapt(header);
			toolkit.paintBordersFor(header);
			{
				Label lblTitel = new Label(header, SWT.NONE);
				lblTitel.setAlignment(SWT.CENTER);
				GridData gd_lblTitel = new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1);
				gd_lblTitel.heightHint = 14;
				lblTitel.setLayoutData(gd_lblTitel);
				toolkit.adapt(lblTitel, true, true);
				lblTitel.setText("Titel");
				lblTitel.setForeground(WHITE);
				lblTitel.setBackground(RED);
			}
			{
				Label lblPreis = new Label(header, SWT.NONE);
				lblPreis.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, true, 1, 1));
				toolkit.adapt(lblPreis, true, true);
				lblPreis.setText("Preis");
				lblPreis.setBackground(RED);
				lblPreis.setForeground(WHITE);
			}

			header.setBackground(RED);
		}
		{
			Composite body = new Composite(composite, SWT.NONE);
			GridData gd_body = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
			gd_body.heightHint = 39;
			body.setLayoutData(gd_body);
			toolkit.adapt(body);
			toolkit.paintBordersFor(body);
			body.setLayout(new GridLayout(1, false));
			
			Label lblBeschreibung = new Label(body, SWT.NONE);
			lblBeschreibung.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			toolkit.adapt(lblBeschreibung, true, true);
			lblBeschreibung.setText("Beschreibung");
		}
		{
			Composite footer = new Composite(composite, SWT.NONE);
			GridData gd_footer = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
			gd_footer.heightHint = 120;
			footer.setLayoutData(gd_footer);
			toolkit.adapt(footer);
			toolkit.paintBordersFor(footer);
			footer.setLayout(new GridLayout(2, false));
			
			Label lblAnzahl = new Label(footer, SWT.NONE);
			lblAnzahl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
			toolkit.adapt(lblAnzahl, true, true);
			lblAnzahl.setText("Anzahl");
			
			text = new Text(footer, SWT.BORDER);
			text.setText("0");
			text.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
			toolkit.adapt(text, true, true);
			
			Label lblPreisProStck = new Label(footer, SWT.NONE);
			lblPreisProStck.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			toolkit.adapt(lblPreisProStck, true, true);
			lblPreisProStck.setText("Preis pro Stück");
			
			Label label_1 = new Label(footer, SWT.NONE);
			label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			toolkit.adapt(label_1, true, true);
			label_1.setText("0,00");
			
			Label lblGesamtpreis = new Label(footer, SWT.NONE);
			lblGesamtpreis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			toolkit.adapt(lblGesamtpreis, true, true);
			lblGesamtpreis.setText("Gesamtpreis");
			
			Label label = new Label(footer, SWT.NONE);
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			toolkit.adapt(label, true, true);
			label.setText("0,00");
			new Label(footer, SWT.NONE);
			new Label(footer, SWT.NONE);
			new Label(footer, SWT.NONE);
			
			Button btnHinzufgen = new Button(footer, SWT.NONE);
			btnHinzufgen.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			toolkit.adapt(btnHinzufgen, true, true);
			btnHinzufgen.setText("Hinzufügen");
		}
	}
	
	private void creatAuswahl(Composite parent) {
		
		Composite composite = new Composite(parent, SWT.NONE);
		toolkit.adapt(composite);
		toolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(1, false));
		auswahl = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
		auswahl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		TableViewer tAuswahl = new TableViewer(auswahl);
		
		toolkit.adapt(auswahl);
		toolkit.paintBordersFor(auswahl);
		
		TableColumn tblclmnAuswahlBeschreibung = new TableColumn(auswahl, SWT.NONE);
		tblclmnAuswahlBeschreibung.setText("Auswahl");
		TableColumn tblclmnAuswahlPreis = new TableColumn(auswahl, SWT.RIGHT);
		tblclmnAuswahlPreis.setText("Preis");
				
		TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33));
        layout.addColumnData(new ColumnWeightData(33));
        layout.addColumnData(new ColumnWeightData(10, 50));
        auswahl.setLayout(layout);
		
		
		tAuswahl.setContentProvider(new ArrayContentProvider());
		tAuswahl.setLabelProvider(new ITableLabelProvider() {
            @Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
            @Override
            public String getColumnText(Object element, int index) {
                Artikel e = (Artikel) element;
                switch (index) {
                case 0:
                    if (e.getKurzbezeichnung() != null) {
                        return e.getKurzbezeichnung();
                    } else {
                        return "";
                    }
                	case 1:
                		return "0,00";
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
		
		TableColumn tblclmnAuswahlEntfernen = new TableColumn(tAuswahl.getTable(), SWT.RIGHT);
		tblclmnAuswahlEntfernen.setWidth(40);
		tblclmnAuswahlEntfernen.setText("Entfernen");
		
		TableViewerColumn actionsNameCol = new TableViewerColumn(tAuswahl, tblclmnAuswahlEntfernen);
        actionsNameCol.setLabelProvider(new ColumnLabelProvider(){
            //make sure you dispose these buttons when viewer input changes
            Map<Object, Button> buttons = new HashMap<Object, Button>();


            @Override
            public void update(ViewerCell cell) {

                TableItem item = (TableItem) cell.getItem();
                Button button;
                if(buttons.containsKey(cell.getElement()))
                {
                    button = buttons.get(cell.getElement());
                }
                else
                {
                     button = new Button((Composite) cell.getViewerRow().getControl(),SWT.LEFT);
                    button.setText("Entfernen");
                    buttons.put(cell.getElement(), button);
                }
                TableEditor editor = new TableEditor(item.getParent());
                editor.grabHorizontal  = true;
                editor.grabVertical = true;
                editor.setEditor(button , item, cell.getColumnIndex());
                editor.layout();
            }

        });
		
		tAuswahl.setInput(this.artikelService.findAll());
        
        createCheckout(composite);
		
	}

	private void createCheckout(Composite composite) {
		Composite checkout = new Composite(composite, SWT.NONE);
		GridData gd_checkout = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_checkout.heightHint = 42;
		checkout.setLayoutData(gd_checkout);
		toolkit.adapt(checkout);
		toolkit.paintBordersFor(checkout);
		checkout.setLayout(new GridLayout(3, false));
		
		Label lblGesamtpreis_1 = new Label(checkout, SWT.NONE);
		lblGesamtpreis_1.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		toolkit.adapt(lblGesamtpreis_1, true, true);
		lblGesamtpreis_1.setText("Gesamtpreis");
		
		Label label = new Label(checkout, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, true, 1, 1));
		toolkit.adapt(label, true, true);
		label.setText("0,00");
		
		Button btnBezahlen = new Button(checkout, SWT.NONE);
		toolkit.adapt(btnBezahlen, true, true);
		btnBezahlen.setText("Bezahlen");
	}

	private void createTable(Composite parent) {
    
        this.merchandiseList = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33));
        this.merchandiseList.getTable().setLayout(layout);
    
        this.merchandiseList.getTable().setLinesVisible(true);
        this.merchandiseList.getTable().setHeaderVisible(true);
    
        this.merchandiseList.setContentProvider(new ArrayContentProvider());
        this.merchandiseList.setLabelProvider(new ITableLabelProvider() {
            @Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
            @Override
            public String getColumnText(Object element, int index) {
                Artikel e = (Artikel) element;
                switch (index) {
                case 0:
                    if (e.getKurzbezeichnung() != null) {
                        return e.getKurzbezeichnung();
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
               
        TableColumn column1 = new TableColumn(merchandiseList.getTable(), SWT.NONE);
        column1.setText("Merchandiseartikel");
        
        // MAGIC HAPPENS HERE
        this.artikelService = new ArtikelServiceImpl((ArtikelDao)DaoFactory.getByEntity(Artikel.class));
        
        this.merchandiseList.setInput(this.artikelService.findAll());

    
        this.toolkit.adapt(this.merchandiseList.getTable(), true, true);
    }

	@PreDestroy
	public void dispose() {
	}
}
