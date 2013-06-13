package at.ticketline.kassa.ui.wizard;

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

import at.ticketline.service.api.KundeService;

public class TransaktionWizardSeiteZwei extends WizardPage implements Listener{

    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteZwei.class);
    
    @Inject private KundeService kundeService;
    
    private Button btnNeuerKunde;
    private Button btnBestehenderKunde;
    private Button btnAnonymerKunde;

    /**
     * Auf dieser Seite kann zwischem neuen, bestehenden oder
     * anonymen Kunden für die Transaktion entschieden werden
     */
    public TransaktionWizardSeiteZwei() {
        super("kundenwahl");
        setTitle("Kundentyp");
        setDescription("Wähle die Art des Kunden aus");
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
        
        btnNeuerKunde = new Button(container, SWT.RADIO);
        btnNeuerKunde.addListener(SWT.Selection, this);
        btnNeuerKunde.setText("Neuer Kunde");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblNeu1 = new Label(container, SWT.NONE);
        lblNeu1.setText("Ist der Kunde noch nicht bei uns registriert? Überzeugen ");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblNeu2 = new Label(container, SWT.NONE);
        lblNeu2.setText("sie ihn von einer Kundenkarte und wählen diese Option!");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnBestehenderKunde = new Button(container, SWT.RADIO);
        btnBestehenderKunde.addListener(SWT.Selection, this);
        btnBestehenderKunde.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
        btnBestehenderKunde.setText("Bestehender Kunde");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblBestehend1 = new Label(container, SWT.NONE);
        lblBestehend1.setText("Wenn der Kunde bereits eine Kundenkarte besitzt,");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblBestehend2 = new Label(container, SWT.NONE);
        lblBestehend2.setText(" ist diese Option zu wählen.");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        btnAnonymerKunde = new Button(container, SWT.RADIO);
        btnAnonymerKunde.setText("Anonymer Kunde");
        btnAnonymerKunde.addListener(SWT.Selection, this);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblAnon1 = new Label(container, SWT.NONE);
        lblAnon1.setText("Will der Kunde keine Kundenkarte, kann er auch anonym ");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblAnon2 = new Label(container, SWT.NONE);
        lblAnon2.setText("Karten bestellen.");
        
        //TODO set completed when customer type is selected
        
        setPageComplete(false);
        LOG.info("Wizardseite zur Auswahl des Kudnentyps erstellt!");
    }

    @Override
    public void handleEvent(Event e) {
        if(e.widget == btnNeuerKunde) {
            setPageComplete(true);
        } else if(e.widget == btnAnonymerKunde) {
            setPageComplete(true);
        } else if(e.widget == btnBestehenderKunde) {
            setPageComplete(true);
        }        
    }
    
    /**
     * Gibt die jeweilige nächste Seite gemaess
     * Radio-Button Auswahl zurueck
     */
    @Override
    public WizardPage getNextPage(){
        if (btnNeuerKunde.getSelection()) {
            return ((TransaktionWizard)getWizard()).drei;
        }
        if (btnBestehenderKunde.getSelection()) { 
            return ((TransaktionWizard)getWizard()).vier;
        }
        if (btnAnonymerKunde.getSelection()) { 
            WizardPage fuenf = ((TransaktionWizard)getWizard()).fuenf;
            return fuenf;
        }
        return null;
     }
}
