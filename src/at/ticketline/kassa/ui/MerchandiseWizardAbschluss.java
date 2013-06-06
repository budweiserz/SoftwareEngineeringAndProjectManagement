package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    //TODO gets called when wizard is complete
    public void doTransaction() {
        
    }
    
    public void updateContent() {
        if(values.getKunde() != null) {
            lblName.setText("Name: " + values.getKunde().getVorname() + " " + values.getKunde().getVorname());
        } else {
            lblName.setText("Anonymer Kunde");
        }
        String zahlung = values.getZahlungsart().toString();
        lblZahlungsk.setText(zahlung.substring(0, 1) + zahlung.substring(1).toLowerCase());
    }
}
