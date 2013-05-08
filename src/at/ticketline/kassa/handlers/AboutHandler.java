package at.ticketline.kassa.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class AboutHandler {
    @Execute
    public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell) {
        MessageDialog.openInformation(shell, "Über Ticketline RCP", "Gruppe XX");
    }
}
