package at.ticketline.kassa.ui.wizard;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Artikel;
import at.ticketline.entity.Praemie;
import at.ticketline.service.api.KundeService;

@SuppressWarnings("restriction")
public class MerchandiseWizard extends Wizard implements IPageChangedListener {
    
    @Inject private KundeService kundeService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    
    @Inject
    @Named(IServiceConstants.ACTIVE_SHELL)
    private Shell shell;

    private static final Logger LOG = LoggerFactory.getLogger(MerchandiseWizard.class);

    protected MerchandiseWizardSeiteZwei zwei;
    protected MerchandiseWizardSeiteDrei drei;
    protected MerchandiseWizardSeiteVier vier;
    protected MerchandiseWizardAbschluss fuenf;
    protected MerchandiseWizardZahlung zahlung;
    
    private MerchandiseWizardValues values;
    
    @Inject
    public MerchandiseWizard() {
        super();
        values = new MerchandiseWizardValues();
        setWindowTitle("Merchandise Wizard");
        setNeedsProgressMonitor(true);
    }
    

    @Override
    public void addPages() {
        LOG.info("Add Pages to Wizard...");
        zwei = new MerchandiseWizardSeiteZwei(values);
        drei = new MerchandiseWizardSeiteDrei(values);
        vier  = new MerchandiseWizardSeiteVier(values);
        fuenf = new MerchandiseWizardAbschluss(values);
        zahlung = new MerchandiseWizardZahlung(values);
        vier.setKundeService(kundeService);
        vier.setESelectionService(selectionService);
        drei.setCommandService(commandService);
        drei.setDirty(dirty);
        drei.setHandlerService(handlerService);
        drei.setKundeService(kundeService);
        addPage(zwei);
        addPage(drei);
        addPage(vier);
        addPage(zahlung);
        addPage(fuenf);
        
//        MerchandiseWizardDialog mwd = ((MerchandiseWizardDialog)getContainer());
//        mwd.addPageChangingListener(this);        
    }

    @Override
    public boolean performCancel() {
       // if(fuenf.isCurrentPage())
      //      return false;// TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public boolean canFinish() {
        return fuenf.isPageComplete();
//        if(fuenf.isCurrentPage()) {
//            //fuenf.setPageComplete(true);
//            return true;
//        }
//        return false;
    }
    
    public void setDialogListener() {
        LOG.info("set Listener to Wizard Container (" + getContainer().getClass().getName() + ")");
        WizardDialog wd = (WizardDialog) getContainer();
        wd.addPageChangedListener(this);
    }
 
    @Override
    public boolean performFinish() {
        return true;
    }


    @Override
    public void pageChanged(PageChangedEvent e) {
        if(e.getSelectedPage() == fuenf) {
            fuenf.updateContent();
            
            if(values.getZahlungsart() == null) {
                LOG.debug("something went wrong");
            }
            fuenf.doTransaction();
            LOG.info("Merchandise Wizard erfolgreich abgeschlossen!");
            fuenf.setPageComplete(true);
        }
        
    }
    
    public void setSelectedArtikel(HashMap<Artikel, Integer> selected) {
        values.setSelected(selected);
    }


//    @Override
//    public void handlePageChanging(PageChangingEvent event) {
//        if(event.getCurrentPage() == drei && event.getTargetPage() == zahlung) {
//            if(!drei.saveNewKunde()) {
//                LOG.debug("HEYO");
//                ((MerchandiseWizardDialog) getContainer()).close();                
//            }
//                 
//         }        
//    }

//    @Override
//    public boolean performFinish() {
//        if(fuenf.isCurrentPage()) {
//            fuenf.setPageComplete(true);
//            return true;
//        }
//        return false;
//    }
}
