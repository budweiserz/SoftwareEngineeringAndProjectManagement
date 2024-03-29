package at.ticketline.kassa.ui.wizard;

import java.util.Set;

import javax.inject.Inject;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;
import at.ticketline.service.api.TransaktionService;
import at.ticketline.service.impl.TransaktionServiceImpl;

public class TicketWizardAbschluss extends WizardPage {

    private TicketWizardValues values;
    private Label lblReservierung;
    private Label lblName;
    private Label lblHinweis;
    private Label lblAuffhrung;
    private Label lblKunde;
    @Inject TransaktionService transaktionsService;
    private static final Logger LOG = LoggerFactory.getLogger(TicketWizardAbschluss.class);
    
    /**
     * Diese Seite wird nach einer erfolgreichen Transaktion angezeigt.
     * Bei einem Kauf werden der Preis angezeigt, bei einer Reservierung der
     * Preis und die Reservierungsnummer.
     */
    public TicketWizardAbschluss(TicketWizardValues values2) {
        super("Transaktionanzeige");
        setTitle("Transaktion abgeschlossen");
        setDescription("Hier sind die Informationen zur Transaktion:");
        this.values = values2;
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(null);
        
        lblReservierung = new Label(container, SWT.NONE);
        lblReservierung.setBounds(5, 5, 389, 15);
        lblReservierung.setText("BlankFeld");
        
        lblName = new Label(container, SWT.NONE);
        lblName.setBounds(15, 47, 389, 15);
        lblName.setText("Blankfeld");
        
        lblHinweis = new Label(container, SWT.NONE);
        lblHinweis.setBounds(15, 126, 530, 15);
        lblHinweis.setText("Blankfeld");
        
        lblAuffhrung = new Label(container, SWT.NONE);
        lblAuffhrung.setBounds(5, 105, 559, 15);
        lblAuffhrung.setText("Aufführung:");
        
        lblKunde = new Label(container, SWT.NONE);
        lblKunde.setBounds(5, 26, 55, 15);
        lblKunde.setText("Kunde:");
        
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
        TransaktionService service = new TransaktionServiceImpl((TransaktionDao) DaoFactory.getByEntity(Transaktion.class));
        
		//Mitarbeiter mitarbeiter = ((MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class)).findAll().get(0);
        Mitarbeiter mitarbeiter = values.getMitarbeiter();
        Kunde kunde = values.getKunde();
        Auffuehrung auffuehrung = values.getAuffuehrung();
        Set<Platz> plaetze = values.getPlaetze();
            
        Transaktion t = values.getTransaktion();
        service.cancelReservation(t.getReservierungsnr());
        t = service.sell(mitarbeiter, kunde, auffuehrung, plaetze);

        values.setReservierungsNummer(t.getReservierungsnr());
        this.updateContent();
        values.getPart().updateList();
    }
    
    public void updateContent() {

        /**
         * @see: TransaktionWizardSeiteFuenf.java
         */
        if(values.isReservierung()) {
            setTitle("Reservierung abgeschlossen");
            setDescription("Hier sind die Informationen zur Reservierung:");
            lblReservierung.setText("Reservierungsnummer: " + values.getReservierungsNummer());
        } else {
            setTitle("Buchung abgeschlossen");
            setDescription("Hier sind die Informationen zur Transaktion:");
            lblReservierung.setText("Buchungsnummer: " + values.getReservierungsNummer());
        }

        if(values.getKunde() != null) {
            lblName.setText("Name: " + values.getKunde().getVorname() + " " + values.getKunde().getNachname());
        } else {
            lblName.setText("Anonymer Kunde");
        }
        lblHinweis.setText(values.getAuffuehrung().getHinweis());
    }
}
