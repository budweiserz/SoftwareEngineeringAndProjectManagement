package at.ticketline.kassa.ui;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.wizard.Wizard;

import at.ticketline.service.api.KundeService;

public class TransaktionWizard extends Wizard {
    
    @Inject private KundeService kundeService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    
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
        zwei.setKundeService(kundeService);
        addPage(eins);
        addPage(zwei);
    }

    @Override
    public boolean performFinish() {
        return false;
    }

}
