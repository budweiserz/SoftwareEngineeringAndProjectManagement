package at.ticketline.kassa.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

@SuppressWarnings("restriction")
public class OpenNewKuenstlerHandler extends NewTabHandler {

    @Inject MApplication application;

    public OpenNewKuenstlerHandler() {
        super("at.ticketline.partdescriptor.newkuenstler", "Neuer Künstler", "Neuen Künstler anlegen");
    }

    @Override
    protected void preExecute() {
    	IEclipseContext context = application.getContext();
    	context.set("newKuenstler", true);
    }
}