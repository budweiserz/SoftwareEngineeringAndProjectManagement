package at.ticketline.kassa.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

@SuppressWarnings("restriction")
public class OpenKuenstlerHandler extends NewTabHandler {

    @Inject MApplication application;

    public OpenKuenstlerHandler() {
        super("at.ticketline.partdescriptor.newkuenstler", "Neuer Kuenstler", "Neuen Kuenstler anlegen");
    }

    @Override
    protected void preExecute() {
    	IEclipseContext context = application.getContext();
    	context.set("newKuenstler", false);
    }
}