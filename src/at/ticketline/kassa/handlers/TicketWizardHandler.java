package at.ticketline.kassa.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Mitarbeiter;
import at.ticketline.kassa.ui.TicketWizard;
import at.ticketline.kassa.ui.wizard.TransaktionWizardDialog;

@SuppressWarnings("restriction")
public class TicketWizardHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TicketWizardHandler.class);
    @Execute
    public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        TicketWizard tw = ContextInjectionFactory.make(TicketWizard.class, context);
        
        // XXX: Hack to set the currently logged in mitarbeiter in the wizard
        tw.setMitarbeiter((Mitarbeiter)context.get("login")); 
        
        TransaktionWizardDialog transaktion = new TransaktionWizardDialog(shell, tw);
        tw.setDialogListener();
        if(transaktion.open() == Window.OK) {
            LOG.info("Opened the Ticket wizard!");
        }
    }
}
