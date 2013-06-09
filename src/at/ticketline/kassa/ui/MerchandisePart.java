package at.ticketline.kassa.ui;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.dao.api.BestellungDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;
<<<<<<< HEAD
import at.ticketline.entity.Zahlungsart;
=======
import at.ticketline.entity.Merchandise;
import at.ticketline.entity.Praemie;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.MerchandiseService;
>>>>>>> Introduce entity Merchandise #23219
import at.ticketline.service.api.BestellungService;
import at.ticketline.service.impl.ArtikelServiceImpl;
import at.ticketline.service.impl.BestellungServiceImpl;

@SuppressWarnings("restriction")
public class MerchandisePart {
    
    private static final Logger LOG = LoggerFactory.getLogger(MerchandisePart.class);

    private FormToolkit toolkit;
<<<<<<< HEAD
    private TableViewer merchandiseList;
    private ArtikelServiceImpl artikelService;
    private Table auswahl;
    private HashMap<Artikel, Integer> selected = new HashMap<Artikel, Integer>();
=======

    @Inject
    private MerchandiseService merchandiseService;
    @Inject
    private PraemieService praemieService;
>>>>>>> Introduce entity Merchandise #23219

    private static final Device device = Display.getCurrent();
    private static final Color RED = new Color(device, 170, 34, 34);
    private static final Color WHITE = new Color(device, 255, 255, 255);
    private Text aAnzahlInput;
    private Label aLblTitel;
    private Label aLblPreis;
    private Label aLblBeschreibung;
    private Label aLblAnzahl;
    private Label aLblGesamtpreis;
    private Label aGesamtpreis;
    private Button aBtnHinzufgen;
    private DecimalFormat formatter = new DecimalFormat("###.##");
    private Artikel currentSelection = null;

    private TableViewer tAuswahl;
    @Inject private MPart activePart;
    private Composite parent;
    private Label overallPrice;

    public MerchandisePart() {
    }

    /**
     * Create contents of the view part.
     */
    @PostConstruct
    public void createControls(Composite parent) {
        this.parent = parent;
        parent.setLayout(new FillLayout(SWT.HORIZONTAL));
        this.toolkit = new FormToolkit(parent.getDisplay());

<<<<<<< HEAD
        this.createTable(parent);
=======
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
>>>>>>> Introduce entity Merchandise #23219

        createMiddleComposite(parent);

        this.creatAuswahl(parent);

<<<<<<< HEAD
=======
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
>>>>>>> Introduce entity Merchandise #23219
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
                this.aLblTitel = new Label(header, SWT.WRAP);
                GridData gd_aLblTitel = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
                gd_aLblTitel.heightHint = 14;
                aLblTitel.setLayoutData(gd_aLblTitel);
                toolkit.adapt(aLblTitel, true, true);
                aLblTitel.setText("Titel");
                aLblTitel.setForeground(WHITE);
                aLblTitel.setBackground(RED);
            }
            {
                this.aLblPreis = new Label(header, SWT.NONE);
                aLblPreis.setAlignment(SWT.RIGHT);
                GridData gd_aLblPreis = new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1);
                gd_aLblPreis.widthHint = 94;
                aLblPreis.setLayoutData(gd_aLblPreis);
                toolkit.adapt(aLblPreis, true, true);
                aLblPreis.setText("0.00€");
                aLblPreis.setBackground(RED);
                aLblPreis.setForeground(WHITE);
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

            this.aLblBeschreibung = new Label(body, SWT.WRAP);
            aLblBeschreibung.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
            toolkit.adapt(aLblBeschreibung, true, true);
            aLblBeschreibung.setText("Beschreibung");
        }
        {
            Composite footer = new Composite(composite, SWT.NONE);
            GridData gd_footer = new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1);
            gd_footer.heightHint = 101;
            footer.setLayoutData(gd_footer);
            toolkit.adapt(footer);
            toolkit.paintBordersFor(footer);
            footer.setLayout(new GridLayout(2, false));

            this.aLblAnzahl = new Label(footer, SWT.NONE);
            aLblAnzahl.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
            toolkit.adapt(aLblAnzahl, true, true);
            aLblAnzahl.setText("Anzahl");

            aAnzahlInput = new Text(footer, SWT.BORDER | SWT.RIGHT);
            aAnzahlInput.setText("0");
            aAnzahlInput.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
            aAnzahlInput.addModifyListener(new ModifyListener() {

                @Override
                public void modifyText(ModifyEvent e) {
                    Text t = (Text)e.widget;
                    if (currentSelection != null) {
<<<<<<< HEAD
                        if (t.getText().equals("0")) {
                            aGesamtpreis.setText("0.00€");
                        } else {
                            try {
                                Integer i = Integer.parseInt(t.getText());
                                float preis = currentSelection.getPreis().floatValue() * i;
                                aGesamtpreis.setText(formatter.format(preis) + "€");
                            } catch (NumberFormatException ex) {
                                LOG.error("Wrong input (no Integer): {}, {}", ex, t.getText());
                            }
=======

                        if (currentSelection instanceof Praemie) {
                        
                        int betrag = ((Praemie) currentSelection).getPunkte().intValue();
                        betrag *= spinner.getSelection();
                        lblZwischenbetrag.setText(formatter.format(betrag) + " Punkte");
                        
                        } else { // Artikel
                          
                            float betrag = ((Merchandise) currentSelection).getPreis().floatValue();
                            betrag *= spinner.getSelection();
                            lblZwischenbetrag.setText(formatter.format(betrag) + " €");
>>>>>>> Introduce entity Merchandise #23219
                        }
                    }
                }
                
            });
            toolkit.adapt(aAnzahlInput, true, true);

            this.aLblGesamtpreis = new Label(footer, SWT.NONE);
            aLblGesamtpreis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
            toolkit.adapt(aLblGesamtpreis, true, true);
            aLblGesamtpreis.setText("Gesamtpreis");

            this.aGesamtpreis = new Label(footer, SWT.NONE);
            aGesamtpreis.setAlignment(SWT.RIGHT);
            aGesamtpreis.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            toolkit.adapt(aGesamtpreis, true, true);
            aGesamtpreis.setText("0.00€");
            new Label(footer, SWT.NONE);
            new Label(footer, SWT.NONE);
            new Label(footer, SWT.NONE);

            this.aBtnHinzufgen = new Button(footer, SWT.NONE);
            aBtnHinzufgen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            toolkit.adapt(aBtnHinzufgen, true, true);
            aBtnHinzufgen.setText("Hinzufügen");
            
            aBtnHinzufgen.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    if (currentSelection != null && !(aAnzahlInput.getText().equals("") ||  aAnzahlInput.getText().equals("0")) && !selected.containsKey(currentSelection)) {
                        try {
                            Integer i = Integer.parseInt(aAnzahlInput.getText());
                            selected.put(currentSelection, i);
                            tAuswahl.setInput(selected.keySet());
                            tAuswahl.refresh();
                            updateOverallPrice();
                        } catch (NumberFormatException ex) {
                            LOG.error("Wrong input (no Integer): {}, {}", ex, aAnzahlInput.getText());
                        }
                    }
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                                        
                }
                
            });
        }
    }

    private void creatAuswahl(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        toolkit.adapt(composite);
        toolkit.paintBordersFor(composite);
        composite.setLayout(new GridLayout(1, false));
        auswahl = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        auswahl.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        this.tAuswahl = new TableViewer(auswahl);

        toolkit.adapt(auswahl);
        toolkit.paintBordersFor(auswahl);

        TableColumn tblclmnAuswahlBeschreibung = new TableColumn(auswahl, SWT.NONE);
        tblclmnAuswahlBeschreibung.setText("Auswahl");
        TableColumn tblclmnAuswahlPreis = new TableColumn(auswahl, SWT.RIGHT);
        tblclmnAuswahlPreis.setText("Preis");

        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33));
        layout.addColumnData(new ColumnWeightData(33));
        layout.addColumnData(new ColumnWeightData(10, 60));
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
<<<<<<< HEAD
                    if (e.getKurzbezeichnung() != null) {
                        float preis = e.getPreis().floatValue();
=======
                    if (e.getKurzbezeichnung() != null && !(e instanceof Praemie)) {
                        float preis = ((Merchandise) e).getPreis().floatValue();
>>>>>>> Introduce entity Merchandise #23219
                        preis *= selected.get(e);
                        return formatter.format(preis);
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

        TableColumn tblclmnAuswahlEntfernen = new TableColumn(tAuswahl.getTable(), SWT.RIGHT);
        tblclmnAuswahlEntfernen.setWidth(60);
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
                    final Object element = cell.getElement();
                    button.addSelectionListener(new SelectionListener() {

                        @Override
                        public void widgetSelected(SelectionEvent e) {
                            Button b = buttons.get(element);
                            b.dispose();
                            buttons.remove(element);
                            selected.remove(element);
                            tAuswahl.setInput(selected.keySet());
                            tAuswahl.refresh();
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

        tAuswahl.setInput(this.selected.keySet());

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

        this.overallPrice = new Label(checkout, SWT.NONE);
        overallPrice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
        toolkit.adapt(overallPrice, true, true);
        overallPrice.setText("0.00€");

        Button btnBezahlen = new Button(checkout, SWT.NONE);
        toolkit.adapt(btnBezahlen, true, true);
        btnBezahlen.setText("Bezahlen");
        btnBezahlen.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LOG.debug("Buy {}", selected);
                // TODO: get Kunde and Zahlungsart from wizard
                MerchandiseWizard tw = ContextInjectionFactory.make(MerchandiseWizard.class, activePart.getContext());
                MerchandiseWizardDialog transaktion = new MerchandiseWizardDialog(parent.getShell(), tw);
                tw.setDialogListener();
                if(transaktion.open() == Window.OK) {
                    LOG.info("Opened the Merchandise wizard!");
                }
                
                BestellungService bestellungService = new BestellungServiceImpl((BestellungDao)DaoFactory.getByEntity(Bestellung.class));
                //bestellungService.saveBestellungen(selected, Zahlungsart.BANKEINZUG);
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                                
            }
                
        });
    }

    private void createTable(Composite parent) {

        this.merchandiseList = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33));
        this.merchandiseList.getTable().setLayout(layout);

<<<<<<< HEAD
        this.merchandiseList.getTable().setLinesVisible(true);
        this.merchandiseList.getTable().setHeaderVisible(true);
=======
        if (index == 0) {
            tableViewer.setInput(merchandiseService.findAll().toArray());
>>>>>>> Introduce entity Merchandise #23219

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

        merchandiseList.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection)merchandiseList.getSelection();
                
                Artikel firstElement = (Artikel)selection.getFirstElement();
                if (event.getSelection().isEmpty()) {
                    aLblTitel.setText("Titel");
                    aLblPreis.setText("Preis");
                    aLblBeschreibung.setText("Beschreibung");
                    aAnzahlInput.setText("0");
                    aGesamtpreis.setText("0.00€");
                    currentSelection = null;
                } else {
                    if (firstElement.getKurzbezeichnung() != null) {
                        aLblTitel.setText(firstElement.getKurzbezeichnung());
                    } else {
                        aLblTitel.setText("");
                    }
                    
                    if (firstElement.getPreis() != null) {
                        aLblPreis.setText(formatter.format(firstElement.getPreis()) + "€");
                    } else {
                        aLblPreis.setText("");
                    }
                    
                    if (firstElement.getBeschreibung() != null) {
                        aLblBeschreibung.setText(firstElement.getBeschreibung());
                    } else {
                        aLblBeschreibung.setText("");
                    }
                    
                    aAnzahlInput.setText("0");
                    aGesamtpreis.setText("0.00€");
                    
                    currentSelection = firstElement;
                }               
            }

        });

        // MAGIC HAPPENS HERE
        this.artikelService = new ArtikelServiceImpl((ArtikelDao)DaoFactory.getByEntity(Artikel.class));

        this.merchandiseList.setInput(this.artikelService.findAll());


        this.toolkit.adapt(this.merchandiseList.getTable(), true, true);
    }
    
    private void updateOverallPrice() {
        float price = 0;
        
<<<<<<< HEAD
        for (Map.Entry<Artikel, Integer>e : selected.entrySet()) {
            price += ((Artikel)e.getKey()).getPreis().floatValue() * e.getValue();
=======
        for (Map.Entry<Artikel, Integer> e : selected.entrySet()) {
            
            if (e instanceof Praemie) {
                points += ((Praemie) e.getKey()).getPunkte().intValue() * e.getValue();
            } else {
                price += ((Merchandise) e.getKey()).getPreis().floatValue() * e.getValue();
            }
>>>>>>> Introduce entity Merchandise #23219
        }

        this.overallPrice.setText(formatter.format(price) + "€");
    }

    @PreDestroy
    public void dispose() {
    }
}
