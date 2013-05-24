package at.ticketline.kassa.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.kassa.Activator;
import at.ticketline.kassa.ui.TransaktionWizard;
import at.ticketline.kassa.ui.TransaktionWizardSeiteEins;
import at.ticketline.kassa.ui.TransaktionWizardSeiteZwei;
import at.ticketline.service.api.KundeService;

public class OpenTransaktionWizardHandler {
    private static final Logger LOG = LoggerFactory.getLogger(OpenTransaktionWizardHandler.class);

    @SuppressWarnings("restriction")
    @Execute
    public void execute(IEclipseContext context, @Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        TransaktionWizard tw = ContextInjectionFactory.make(TransaktionWizard.class, context);
        WizardDialog transaktion = new WizardDialog(shell, tw);
        if(transaktion.open() == Window.OK) {
            LOG.info("Opened the Transaktions wizard!");
        }
    }
}
