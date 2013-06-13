package at.ticketline.kassa.ui;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.hsqldb.lib.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;
import at.ticketline.service.api.KundeService;

@SuppressWarnings("restriction")
public class TicketWizard extends Wizard implements IPageChangedListener{
    
    @Inject private KundeService kundeService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject
    @Named(IServiceConstants.ACTIVE_SHELL)
    private Shell shell;

    private static final Logger LOG = LoggerFactory.getLogger(TicketWizard.class);

    protected TicketWizardSaalplan eins;
    protected TicketWizardAbschluss fuenf;
    
    private TicketWizardValues values;
    
    @Inject
    public TicketWizard(@ Named (IServiceConstants.ACTIVE_SELECTION) @ Optional Transaktion transaktion) {
        super();
        values = new TicketWizardValues();
        setWindowTitle("Transaktions Wizard");
        setNeedsProgressMonitor(true);
        values.setTransaktion(transaktion);
        for(Platz p : transaktion.getPlaetze()) {
            values.setAuffuehrung(p.getAuffuehrung());
        }
    }
    

    @Override
    public void addPages() {
        LOG.info("Add Pages to Wizard...");
        eins = new TicketWizardSaalplan(values);
        fuenf = new TicketWizardAbschluss(values);
        
        addPage(eins);
        addPage(fuenf);
    }

    @Override
    public boolean performCancel() {
        //if(fuenf.isCurrentPage())
        //    return false;// TODO Auto-generated method stub
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
            fuenf.doTransaction();
            LOG.info("Transaktions Wizard erfolgreich abgeschlossen!");
            fuenf.setPageComplete(true);
        }
        
    }


    /**
     * XXX: Hack to have the currently logged in mitarbeiter in the wizard values
     * @param m The currently logged in mitarbeiter
     */
    public void setMitarbeiter(Mitarbeiter m) {
        this.values.setMitarbeiter(m);
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
