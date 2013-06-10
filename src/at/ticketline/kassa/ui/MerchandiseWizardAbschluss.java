package at.ticketline.kassa.ui;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.BestellungDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.PraemieDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Merchandise;
import at.ticketline.entity.Praemie;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.BestellungService;
import at.ticketline.service.api.KundeService;
import at.ticketline.service.api.PraemieService;
import at.ticketline.service.impl.BestellungServiceImpl;
import at.ticketline.service.impl.KundeServiceImpl;
import at.ticketline.service.impl.PraemieServiceImpl;

public class MerchandiseWizardAbschluss extends WizardPage {

    private MerchandiseWizardValues values;
    private Label lblName;
    private Label lblKunde;
    private Label lblZahlungsart;
    private Label lblZahlungsk;
    private static final Logger LOG = LoggerFactory.getLogger(MerchandiseWizardAbschluss.class);

    /**
     * Diese Seite wird nach einer erfolgreichen Transaktion angezeigt.
     * Bei einem Kauf werden der Preis angezeigt, bei einer Reservierung der
     * Preis und die Reservierungsnummer.
     */
    public MerchandiseWizardAbschluss(MerchandiseWizardValues values) {
        super("Transaktionanzeige");
        setTitle("Transaktion abgeschlossen");
        setDescription("Hier sind die Informationen zur Transaktion:");
        this.values = values;
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(null);
        
        lblName = new Label(container, SWT.NONE);
        lblName.setBounds(15, 47, 389, 15);
        lblName.setText("Blankfeld");
        
        
        lblKunde = new Label(container, SWT.NONE);
        lblKunde.setBounds(5, 26, 55, 15);
        lblKunde.setText("Kunde:");
        
        lblZahlungsart = new Label(container, SWT.NONE);
        lblZahlungsart.setBounds(5, 68, 245, 15);
        lblZahlungsart.setText("Zahlungsart:");
        
        lblZahlungsk = new Label(container, SWT.NONE);
        lblZahlungsk.setBounds(15, 89, 235, 15);
        lblZahlungsk.setText("Blankfeld");
        
        setPageComplete(false);
        LOG.info("Zusammenfassungs - Wizard Seite erstellt!");
    }
    
    @Override
    protected boolean isCurrentPage() {
        // TODO Auto-generated method stub
        return super.isCurrentPage();
    }
    
    @Override
    public  WizardPage getPreviousPage() {
        return null;
    }
    
    // gets called when wizard is complete
    public void doTransaction() {
        int praemieKosten = 0;
        //Kosten aller Praemien
        for (Map.Entry<Artikel, Integer> e : values.getSelected().entrySet()) {
            if (e.getKey() instanceof Praemie)
               praemieKosten += ((Praemie) e.getKey()).getPunkte().intValue();
        }
        KundeService kundeService = new KundeServiceImpl((KundeDao)DaoFactory.getByEntity(Kunde.class));
        Kunde kunde = this.values.getKunde();
        if(praemieKosten > 0) {
            if(kunde != null && kunde.getPunkte() != null && (kunde.getPunkte().intValue() - praemieKosten) >= 0) {
               kunde.setPunkte(new BigDecimal(kunde.getPunkte().intValue() - praemieKosten));
               kundeService.save(kunde);
               //Bestellung abspeichern
               BestellungService bestellungService = new BestellungServiceImpl((BestellungDao)DaoFactory.getByEntity(Bestellung.class));
               bestellungService.saveBestellungen(values.getSelected(), values.getZahlungsart(), values.getKunde());
            } else {
                setErrorMessage();
            }
        } else {
            //Bestellung abspeichern
            BestellungService bestellungService = new BestellungServiceImpl((BestellungDao)DaoFactory.getByEntity(Bestellung.class));
            bestellungService.saveBestellungen(values.getSelected(), values.getZahlungsart(), values.getKunde());
        }
    }
    
    public void updateContent() {
        if(values.getKunde() != null) {
            lblName.setText("Name: " + values.getKunde().getVorname() + " " + values.getKunde().getNachname());
        } else {
            lblName.setText("Anonymer Kunde");
        }
        
        if(values.getZahlungsart() != null) {
            String zahlung = values.getZahlungsart().toString();
            lblZahlungsk.setText(zahlung.substring(0, 1) + zahlung.substring(1).toLowerCase());
        }
    }
    
    public void setErrorMessage() {
            lblZahlungsk.setText("Zu wenige Punkte für Transaktion vorhanden!");
    }
}
