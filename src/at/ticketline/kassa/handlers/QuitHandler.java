package at.ticketline.kassa.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

@SuppressWarnings("restriction")
public class QuitHandler {
    @Execute
    public void execute(IWorkbench workbench, IEclipseContext context,
            @Named(IServiceConstants.ACTIVE_SHELL) Shell shell)
            throws InvocationTargetException, InterruptedException {
        if (MessageDialog.openConfirm(shell, "Confirmation",
                "Wollen Sie das Programm wirklich beenden?")) {
            workbench.close();
        }
    }
}
