package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransaktionWizardSeiteDrei extends WizardPage {

    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteDrei.class);

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
        LOG.info("Erstelle Wizard Seite für neuen Kunden...");
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
    }
    
    @Override
    public WizardPage getNextPage(){
        WizardPage fuenf = ((TransaktionWizard)getWizard()).fuenf;
        return fuenf;
     }

}
