package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TransaktionWizardSeiteZwei extends WizardPage {

    /**
     * Create the wizard.
     */
    public TransaktionWizardSeiteZwei() {
        super("kundenwahl");
        setTitle("Kundentyp");
        setDescription("Wähle die Art des Kunden aus");
    }

    /**
     * Create contents of the wizard.
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        //TODO set completed when customer type is selected
        setPageComplete(true);
    }

}
