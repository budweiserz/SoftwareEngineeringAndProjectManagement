package at.ticketline.kassa.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.wizard.Wizard;

import at.ticketline.entity.Auffuehrung;
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
    
    @Inject
    public TransaktionWizard(@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional Auffuehrung auffuehrung) {
        super();
        values = new TransaktionWizardValues();
        setWindowTitle("Reservierung / Kauf");
        setNeedsProgressMonitor(true);
        values.setAuffuehrung(auffuehrung);
    }
    

    @Override
    public void addPages() {
        eins = new TransaktionWizardSeiteEins(values);
        zwei = new TransaktionWizardSeiteZwei();
        drei = new TransaktionWizardSeiteDrei();
        vier  = new TransaktionWizardSeiteVier(values);
        fuenf = new TransaktionWizardSeiteFuenf(values);
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
