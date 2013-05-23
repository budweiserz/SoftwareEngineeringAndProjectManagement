package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TransaktionWizardSeiteEins extends WizardPage {

    private Composite container;
    
    /**
     * Create the wizard.
     */
    public TransaktionWizardSeiteEins() {
        super("saalplan");
        setTitle("Verfügbare Plätze");
        setDescription("Wählen Sie die gewünschten Plätze aus");
    }

    /**
     * Create contents of the wizard.
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        //TODO Add Saalplan in here... somehow
        
        setControl(container);
        //TODO make true when plaetze selected
        setPageComplete(true);

        
    }
}
