package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TransaktionWizardSeiteDrei extends WizardPage {

    /**
     * Diese Wizard Seite wird aufgerufen, wenn ein neuer Kunde
     * anzulegen ist. Es wird das Formular von KundePart angezeigt.
     * 
     */
    public TransaktionWizardSeiteDrei() {
        super("NeuerKunde");
        setTitle("Neuer Kunde");
        setDescription("Tragen sie die Informationen des Kunden ein:");
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
    }
    
    @Override
    public WizardPage getNextPage(){
        WizardPage fuenf = ((TransaktionWizard)getWizard()).fuenf;
        return fuenf;
     }

}
