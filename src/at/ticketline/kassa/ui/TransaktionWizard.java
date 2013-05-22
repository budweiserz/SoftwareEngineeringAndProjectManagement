package at.ticketline.kassa.ui;

import org.eclipse.jface.wizard.Wizard;

public class TransaktionWizard extends Wizard {

    protected TransaktionWizardSeiteEins eins;
    protected TransaktionWizardSeiteZwei zwei;
    public TransaktionWizard() {
        super();
        setWindowTitle("Reservierung / Kauf");
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        eins = new TransaktionWizardSeiteEins();
        zwei = new TransaktionWizardSeiteZwei();
        addPage(eins);
        addPage(zwei);
    }

    @Override
    public boolean performFinish() {
        return false;
    }

}
