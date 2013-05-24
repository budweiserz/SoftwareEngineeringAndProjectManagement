package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TransaktionWizardSeiteFuenf extends WizardPage {

    /**
     * Diese Seite wird nach einer erfolgreichen Transaktion angezeigt.
     * Bei einem Kauf werden der Preis angezeigt, bei einer Reservierung der
     * Preis und die Reservierungsnummer.
     */
    public TransaktionWizardSeiteFuenf() {
        super("Transaktionanzeige");
        setTitle("Vorgang abgeschlossen");
        setDescription("Hier sind die Informationen zur Transaktion:");
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        
        setPageComplete(true);
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

}
