package at.ticketline.kassa.ui;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.wizard.Wizard;

import at.ticketline.service.api.KundeService;

@SuppressWarnings("restriction")
public class TransaktionWizard extends Wizard {
    
    @Inject private KundeService kundeService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    
    protected TransaktionWizardSeiteEins eins;
    protected TransaktionWizardSeiteZwei zwei;
    protected TransaktionWizardSeiteDrei drei;
    protected TransaktionWizardSeiteVier vier;
    protected TransaktionWizardSeiteFuenf fuenf;
    
    private TransaktionWizardValues values;
    
    public TransaktionWizard() {
        super();
        values = new TransaktionWizardValues();
        setWindowTitle("Reservierung / Kauf");
        setNeedsProgressMonitor(true);
    }

    @Override
    public void addPages() {
        eins = new TransaktionWizardSeiteEins(values);
        zwei = new TransaktionWizardSeiteZwei();
        drei = new TransaktionWizardSeiteDrei();
        vier  = new TransaktionWizardSeiteVier(values);
        fuenf = new TransaktionWizardSeiteFuenf();
        vier.setKundeService(kundeService);
        vier.setECommandService(commandService);
        vier.setEHandlerService(handlerService);
        vier.setESelectionService(selectionService);
        
        addPage(eins);
        addPage(zwei);
        addPage(drei);
        addPage(vier);
        addPage(fuenf);
    }

    @Override
    public boolean performCancel() {
        if(fuenf.isCurrentPage())
            return false;// TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean canFinish() {
        if(fuenf.isCurrentPage()) {
            //fuenf.setPageComplete(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean performFinish() {
        return true;
    }

//    @Override
//    public boolean performFinish() {
//        if(fuenf.isCurrentPage()) {
//            fuenf.setPageComplete(true);
//            return true;
//        }
//        return false;
//    }
}
