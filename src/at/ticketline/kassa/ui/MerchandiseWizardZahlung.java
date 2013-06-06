package at.ticketline.kassa.ui;

import javax.inject.Inject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.KundeService;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class MerchandiseWizardZahlung extends WizardPage implements Listener{

    private static final Logger LOG = LoggerFactory.getLogger(MerchandiseWizardZahlung.class);
    
    @Inject private KundeService kundeService;
    
    private Button btnKreditkarte;
    private Button btnTicketcard;
    private Button btnBankeinzug;
    private Button btnVorkasse;
    private Button btnNachnahme;
    private MerchandiseWizardValues values;

    /**
     * Auf dieser Seite kann zwischem neuen, bestehenden oder
     * anonymen Kunden für die Transaktion entschieden werden
     */
    public MerchandiseWizardZahlung(MerchandiseWizardValues values) {
        super("zahlungsartwahl");
        this.values = values;
        setTitle("Zahlungsart");
        setDescription("Wähle aus, wie der Kunde zahlen will");
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        
        setControl(container);
        container.setLayout(new GridLayout(3, false));
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnKreditkarte = new Button(container, SWT.RADIO);
        btnKreditkarte.addListener(SWT.Selection, this);
        btnKreditkarte.setText("Kreditkarte");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnTicketcard = new Button(container, SWT.RADIO);
        btnTicketcard.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            }
        });
        btnTicketcard.addListener(SWT.Selection, this);
        btnTicketcard.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
        btnTicketcard.setText("Ticketcard");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnBankeinzug = new Button(container, SWT.RADIO);
        btnBankeinzug.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
        btnBankeinzug.setText("Bankeinzug");
        btnBankeinzug.addListener(SWT.Selection, this);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnVorkasse = new Button(container, SWT.RADIO);
        btnVorkasse.setText("Vorkasse");
        btnVorkasse.addListener(SWT.Selection, this);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnNachnahme = new Button(container, SWT.RADIO);
        btnNachnahme.setText("Nachnahme");
        btnNachnahme.addListener(SWT.Selection, this);
        
        //TODO set completed when customer type is selected
        
        setPageComplete(false);
        LOG.info("Wizardseite zur Auswahl des Kudnentyps erstellt!");
    }

    @Override
    public void handleEvent(Event e) {
        if(e.widget == btnKreditkarte) {
            values.setZahlungsart(Zahlungsart.KREDITKARTE);
            setPageComplete(true);
        } else if(e.widget == btnBankeinzug) {
            values.setZahlungsart(Zahlungsart.BANKEINZUG);
            setPageComplete(true);
        } else if(e.widget == btnTicketcard) {
            values.setZahlungsart(Zahlungsart.TICKETCARD);
            setPageComplete(true);
        } else if(e.widget == btnVorkasse) {
            values.setZahlungsart(Zahlungsart.VORKASSE);
            setPageComplete(true);
        } else if(e.widget == btnNachnahme) {
            values.setZahlungsart(Zahlungsart.NACHNAHME);
            setPageComplete(true);
        }        
    }
    
    /**
     * Gibt die jeweilige nächste Seite gemaess
     * Radio-Button Auswahl zurueck
     */
    @Override
    public WizardPage getNextPage(){
        return ((MerchandiseWizard)getWizard()).fuenf;
     }
}
