package at.ticketline.kassa.ui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.BestellungDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Merchandise;
import at.ticketline.entity.Praemie;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.MerchandiseService;
import at.ticketline.service.api.BestellungService;
import at.ticketline.service.api.PraemieService;
import at.ticketline.service.impl.BestellungServiceImpl;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.widgets.Spinner;

public class MerchandisePart {

    private static final Logger LOG = LoggerFactory.getLogger(MerchandisePart.class);

    private FormToolkit toolkit;

    @Inject
    private MerchandiseService merchandiseService;
    @Inject
    private PraemieService praemieService;

    private static final Device device = Display.getCurrent();
    private static final Color RED = new Color(device, 170, 34, 34);
    private static final Color WHITE = new Color(device, 255, 255, 255);
    private Label lblBezeichnung;
    private Label lblPreis;
    private Label lblBeschreibung;
    private Label lblAnzahl;
    Spinner spinner;
    private Label lblZwischenbetrag;
    private Button btnAdd;
    private DecimalFormat formatter = new DecimalFormat("###.##");
    private Artikel currentSelection = null;

    private ComboViewer comboViewer;
    private Combo combo;
    private TableViewer tableViewer;
    private Table table;
    private HashMap<Artikel, Integer> selected = new HashMap<Artikel, Integer>();

    private Label lblGesamtbetrag;

    private TableViewer choiceViewer;
    private Table choice;

    public MerchandisePart() {
    }

    /**
     * Create contents of the view part.
     */
    @PostConstruct
    public void createControls(Composite parent) {
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
        this.toolkit = new FormToolkit(parent.getDisplay());

        createTable(parent);

        createOverview(parent);

        createCart(parent);
    }

    private void createTable(Composite parent) {

        Composite selection_field = new Composite(parent, SWT.NONE);
        toolkit.adapt(selection_field);
        toolkit.paintBordersFor(selection_field);
        selection_field.setLayout(new GridLayout(1, false));

        comboViewer = new ComboViewer(selection_field, SWT.NONE);
        comboViewer.setContentProvider(new ArrayContentProvider());
        comboViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return element.toString();
            }
        });
        String[] choices = { "Merchandise", "Prämien" };
        comboViewer.setInput(choices);

        combo = comboViewer.getCombo();
        combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        toolkit.paintBordersFor(combo);
        combo.select(0);
        combo.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
                updateTable(combo.getSelectionIndex());
            }
        });

        tableViewer = new TableViewer(selection_field, SWT.BORDER | SWT.FULL_SELECTION);
        tableViewer.setContentProvider(new ArrayContentProvider());
        tableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {

                if (element instanceof Artikel) {
                    return ((Artikel) element).getKurzbezeichnung();
                } else {
                    return super.getText(element);
                }
            }
        });
        tableViewer.setInput(merchandiseService.findAll().toArray());
        table = tableViewer.getTable();
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        toolkit.paintBordersFor(table);

        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {

                IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
                Object firstElement;
                Artikel element = null;

                if (selection.isEmpty()) {
                    lblBezeichnung.setText("Bezeichnung");
                    lblPreis.setText("Preis");
                    lblBeschreibung.setText("Beschreibung");
                    spinner.setSelection(0);
                    lblZwischenbetrag.setText("0.00€");
                    currentSelection = null;

                } else {

                    firstElement = selection.getFirstElement();

                    if (firstElement instanceof Artikel) {

                        element = (Artikel) firstElement;

                        lblBezeichnung.setText(element.getKurzbezeichnung());
                        lblBeschreibung.setText(element.getBeschreibung());

                        if (element instanceof Praemie) {
                            lblPreis.setText(((Praemie) element).getPunkte().intValue() + " Punkte");
                        } else {
                            lblPreis.setText(formatter.format(((Merchandise) element).getPreis()) + "€");
                        }
                    }
                    spinner.setSelection(0);
                    lblZwischenbetrag.setText("0.00€");

                    currentSelection = element;
                }
            }

        });
    }

    private void createOverview(Composite parent) {

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
                this.lblBezeichnung = new Label(header, SWT.WRAP);
                GridData gd_lblBezeichnung = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
                gd_lblBezeichnung.heightHint = 14;
                lblBezeichnung.setLayoutData(gd_lblBezeichnung);
                toolkit.adapt(lblBezeichnung, true, true);
                lblBezeichnung.setText("Bezeichnung");
                lblBezeichnung.setForeground(WHITE);
                lblBezeichnung.setBackground(RED);
            }
            {
                this.lblPreis = new Label(header, SWT.NONE);
                lblPreis.setAlignment(SWT.RIGHT);
                GridData gd_lblPreis = new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1);
                gd_lblPreis.widthHint = 94;
                lblPreis.setLayoutData(gd_lblPreis);
                toolkit.adapt(lblPreis, true, true);
                lblPreis.setText("0.00 €");
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

            this.lblBeschreibung = new Label(body, SWT.WRAP);
            lblBeschreibung.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            toolkit.adapt(lblBeschreibung, true, true);
            lblBeschreibung.setText("Beschreibung");
        }
        {
            Composite footer = new Composite(composite, SWT.NONE);
            GridData gd_footer = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
            gd_footer.heightHint = 101;
            footer.setLayoutData(gd_footer);
            toolkit.adapt(footer);
            toolkit.paintBordersFor(footer);
            footer.setLayout(new GridLayout(2, false));

            this.lblAnzahl = new Label(footer, SWT.NONE);
            lblAnzahl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
            toolkit.adapt(lblAnzahl, true, true);
            lblAnzahl.setText("Anzahl");

            spinner = new Spinner(footer, SWT.BORDER);
            toolkit.adapt(spinner);
            toolkit.paintBordersFor(spinner);
            spinner.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (currentSelection != null) {

                        if (currentSelection instanceof Praemie) {
                        
                        int betrag = ((Praemie) currentSelection).getPunkte().intValue();
                        betrag *= spinner.getSelection();
                        lblZwischenbetrag.setText(formatter.format(betrag) + " Punkte");
                        
                        } else { // Artikel
                          
                            float betrag = ((Merchandise) currentSelection).getPreis().floatValue();
                            betrag *= spinner.getSelection();
                            lblZwischenbetrag.setText(formatter.format(betrag) + " €");
                        }
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                }
                
            });

            Label lblZwischenpreis = new Label(footer, SWT.NONE);
            lblZwischenpreis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            toolkit.adapt(lblZwischenpreis, true, true);
            lblZwischenpreis.setText("Zwischenpreis");

            lblZwischenbetrag = new Label(footer, SWT.NONE);
            lblZwischenbetrag.setAlignment(SWT.RIGHT);
            lblZwischenbetrag.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            toolkit.adapt(lblZwischenbetrag, true, true);
            lblZwischenbetrag.setText("0.00€");
            new Label(footer, SWT.NONE);

            this.btnAdd = new Button(footer, SWT.NONE);
            btnAdd.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            toolkit.adapt(btnAdd, true, true);
            btnAdd.setText("Hinzufügen");

            btnAdd.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (currentSelection != null || spinner.getSelection() == 0
                            && !selected.containsKey(currentSelection)) {
                        Integer i = spinner.getSelection();
                        selected.put(currentSelection, i);
                        choiceViewer.setInput(selected.keySet());
                        choiceViewer.refresh();
                        updateOverallPrice();
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {

                }

            });
        }
    }

    private void createCart(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        toolkit.adapt(composite);
        toolkit.paintBordersFor(composite);
        composite.setLayout(new GridLayout(1, false));
        choice = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        choice.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        this.choiceViewer = new TableViewer(choice);

        toolkit.adapt(choice);
        toolkit.paintBordersFor(choice);

        TableColumn tblclmnAuswahlBeschreibung = new TableColumn(choice, SWT.NONE);
        tblclmnAuswahlBeschreibung.setText("Auswahl");
        TableColumn tblclmnAuswahlPreis = new TableColumn(choice, SWT.RIGHT);
        tblclmnAuswahlPreis.setText("Preis");
        TableColumn tblclmnAuswahlPunkte = new TableColumn(choice, SWT.RIGHT);
        tblclmnAuswahlPunkte.setText("Punkte");

        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(50));
        layout.addColumnData(new ColumnWeightData(20));
        layout.addColumnData(new ColumnWeightData(20));
        layout.addColumnData(new ColumnWeightData(10,60));        
        choice.setLayout(layout);


        choiceViewer.setContentProvider(new ArrayContentProvider());
        choiceViewer.setLabelProvider(new ITableLabelProvider() {
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
                    if (e.getKurzbezeichnung() != null && !(e instanceof Praemie)) {
                        float preis = ((Merchandise) e).getPreis().floatValue();
                        preis *= selected.get(e);
                        return formatter.format(preis);
                    } else {
                        return "";
                    }
                case 2:
                    if (e instanceof Praemie && ((Praemie) e).getPunkte() != null) {                        
                        return "" + ((Praemie) e).getPunkte().intValue();
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

        TableColumn tblclmnAuswahlEntfernen = new TableColumn(choiceViewer.getTable(), SWT.RIGHT);
        tblclmnAuswahlEntfernen.setWidth(60);
        tblclmnAuswahlEntfernen.setText("Entfernen");

        TableViewerColumn actionsNameCol = new TableViewerColumn(choiceViewer, tblclmnAuswahlEntfernen);
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
                    final Object element = cell.getElement();
                    button.addSelectionListener(new SelectionListener() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            Button b = buttons.get(element);
                            b.dispose();
                            buttons.remove(element);
                            selected.remove(element);
                            choiceViewer.setInput(selected.keySet());
                            choiceViewer.refresh();
                            updateOverallPrice();
                        }

                        @Override
                        public void widgetDefaultSelected(SelectionEvent e) {

                        }

                    });
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

        choiceViewer.setInput(this.selected.keySet());
        createCheckout(composite);
    }

    private void createCheckout(Composite composite) {
        
        Composite checkout = new Composite(composite, SWT.NONE);
        GridData gd_footer = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
        gd_footer.heightHint = 50;
        checkout.setLayoutData(gd_footer);
        toolkit.adapt(checkout);
        toolkit.paintBordersFor(checkout);
        checkout.setLayout(new GridLayout(3, false));

        toolkit.adapt(checkout);
        toolkit.paintBordersFor(checkout);

        Label lblGesamtpreis = new Label(checkout, SWT.NONE);
        lblGesamtpreis.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1));
        toolkit.adapt(lblGesamtpreis, true, true);
        lblGesamtpreis.setText("Gesamtpreis");
        
        this.lblGesamtbetrag = new Label(checkout, SWT.RIGHT);
        GridData gd_lblGesamtbetrag = new GridData(SWT.LEFT, SWT.TOP, false, true, 1, 1);
        gd_lblGesamtbetrag.minimumWidth = 60;
        lblGesamtbetrag.setLayoutData(gd_lblGesamtbetrag);
        toolkit.adapt(lblGesamtbetrag, true, true);
        lblGesamtbetrag.setText("0.00€");
        
        
        Button btnBezahlen = new Button(checkout, SWT.NONE);
        btnBezahlen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        toolkit.adapt(btnBezahlen, true, true);
        btnBezahlen.setText("Bezahlen");
        btnBezahlen.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LOG.debug("Buy {}", selected);
                // TODO: get Kunde and Zahlungsart from wizard
                BestellungService bestellungService = new BestellungServiceImpl((BestellungDao)DaoFactory.getByEntity(Bestellung.class));
                bestellungService.saveBestellungen(selected, Zahlungsart.BANKEINZUG);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                                
            }
                
        });
    }


    private void updateTable(int index) {

        if (index == 0) {
            tableViewer.setInput(merchandiseService.findAll().toArray());

        } else { // index == 1

            tableViewer.setInput(praemieService.findAll().toArray());
        }
    }

    private void updateOverallPrice() {
        float price = 0;
        int points = 0;
        
        for (Map.Entry<Artikel, Integer> e : selected.entrySet()) {
            
            if (e instanceof Praemie) {
                points += ((Praemie) e.getKey()).getPunkte().intValue() * e.getValue();
            } else {
                price += ((Merchandise) e.getKey()).getPreis().floatValue() * e.getValue();
            }
        }

        this.lblGesamtbetrag.setText(formatter.format(price) + " €\n + " + points + " Punkte");
    }

    @PreDestroy
    public void dispose() {
    }
}