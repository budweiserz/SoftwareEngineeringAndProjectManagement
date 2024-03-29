package at.ticketline.kassa.ui.wizard;

import java.util.Map;

import javax.inject.Inject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Artikel;
import at.ticketline.entity.Praemie;
import at.ticketline.service.api.KundeService;

public class MerchandiseWizardSeiteZwei extends WizardPage implements Listener{

    private static final Logger LOG = LoggerFactory.getLogger(MerchandiseWizardSeiteZwei.class);
    
    @Inject private KundeService kundeService;
    
    private Button btnNeuerKunde;
    private Button btnBestehenderKunde;
    private Button btnAnonymerKunde;
    private MerchandiseWizardValues values;
    
    /**
     * Auf dieser Seite kann zwischem neuen, bestehenden oder
     * anonymen Kunden für die Transaktion entschieden werden
     */
    public MerchandiseWizardSeiteZwei(MerchandiseWizardValues values) {
        super("kundenwahl");
        setTitle("Kundentyp");
        setDescription("Wähle die Art des Kunden aus");
        this.values = values;
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    @Override
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
        btnNeuerKunde.addSelectionListener(new KundenartListener());
        
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
        btnBestehenderKunde.addSelectionListener(new KundenartListener());

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
        btnAnonymerKunde.addSelectionListener(new KundenartListener());
        
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblAnon1 = new Label(container, SWT.NONE);
        lblAnon1.setText("Will der Kunde keine Kundenkarte, kann er auch anonym ");
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        
        Label lblAnon2 = new Label(container, SWT.NONE);
        lblAnon2.setText("Karten bestellen.");
        
        setPageComplete(false);
 
        LOG.info("Wizardseite zur Auswahl des Kudnentyps erstellt!");
    }

    @Override
    public void handleEvent(Event e) {
        
        /*
        if(e.widget == btnNeuerKunde) {
            setPageComplete(true);
        } else if(e.widget == btnAnonymerKunde) {
            setPageComplete(true);
        } else if(e.widget == btnBestehenderKunde) {
            setPageComplete(true);
        }  
       */      
    }
    
    /**
     * Gibt die jeweilige nächste Seite gemaess
     * Radio-Button Auswahl zurueck
     */
    @Override
    public WizardPage getNextPage(){
        if (btnNeuerKunde.getSelection()) {
            return ((MerchandiseWizard)getWizard()).drei;
        }
        if (btnBestehenderKunde.getSelection()) { 
            return ((MerchandiseWizard)getWizard()).vier;
        }
        if (btnAnonymerKunde.getSelection()) { 
            WizardPage fuenf = ((MerchandiseWizard)getWizard()).zahlung;
            return fuenf;
        }
        return null;
     }
    
    private class KundenartListener implements SelectionListener {

        @Override
        public void widgetSelected(SelectionEvent e) {
        
            if (e.getSource() == MerchandiseWizardSeiteZwei.this.btnNeuerKunde) {
                
                if (selectedContainsPraemien()){
                    MerchandiseWizardSeiteZwei.this.setErrorMessage("Einkauf enthält Prämien: Nur bestehender Kunde wählbar.");
                    MerchandiseWizardSeiteZwei.this.setPageComplete(false);
                } else {
                    MerchandiseWizardSeiteZwei.this.setPageComplete(true);     
                }
            }
            if (e.getSource() == MerchandiseWizardSeiteZwei.this.btnAnonymerKunde) {
                if (selectedContainsPraemien()){
                    MerchandiseWizardSeiteZwei.this.setErrorMessage("Einkauf enthält Prämien: Nur bestehender Kunde wählbar.");
                    MerchandiseWizardSeiteZwei.this.setPageComplete(false);
                } else {
                    MerchandiseWizardSeiteZwei.this.setPageComplete(true);     
                }
            }
            if (e.getSource() == MerchandiseWizardSeiteZwei.this.btnBestehenderKunde) {
                MerchandiseWizardSeiteZwei.this.setErrorMessage(null); // clear error message
                MerchandiseWizardSeiteZwei.this.setPageComplete(true);
            }
        }

        @Override
        public void widgetDefaultSelected(SelectionEvent e) {
            // TODO Auto-generated method stub
            
        }   
    }
    
    private boolean selectedContainsPraemien() {
        
        boolean output = false;
        
        for (Map.Entry<Artikel, Integer> e : values.getSelected().entrySet()) {
            LOG.debug(e.getClass().getName());
            if (e.getKey() instanceof Praemie) {
                output = true;
            }
        }
        return output;
    }
}