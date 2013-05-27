package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransaktionWizardSeiteFuenf extends WizardPage {

    private TransaktionWizardValues values;
    private Label lblReservierung;
    private Label lblName;
    private Label lblHinweis;
    private Label lblAuffhrung;
    private Label lblKunde;
    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteFuenf.class);

    /**
     * Diese Seite wird nach einer erfolgreichen Transaktion angezeigt.
     * Bei einem Kauf werden der Preis angezeigt, bei einer Reservierung der
     * Preis und die Reservierungsnummer.
     */
    public TransaktionWizardSeiteFuenf(TransaktionWizardValues values) {
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
    
    //TODO gets called when wizard is complete
    public void doTransaction() {
        
    }
    
    public void updateContent() {
        if(values.isReservierung()) {
            setTitle("Reservierung abgeschlossen");
            setDescription("Hier sind die Informationen zur Reservierung:");
            lblReservierung.setText("Reservierungsnummer: ");
        } else {
            setTitle("Buchung abgeschlossen");
            setDescription("Hier sind die Informationen zur Transaktion:");
            lblReservierung.setText("");
        }
        if(values.getKunde() != null) {
            lblName.setText("Name: " + values.getKunde().getVorname() + " " + values.getKunde().getVorname());
        } else {
            lblName.setText("Anonymer Kunde");
        }
        lblHinweis.setText(values.getAuffuehrung().getHinweis());
    }
}
