package at.ticketline.kassa.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;

@SuppressWarnings("restriction")
public class OpenNewKundeHandler extends NewTabHandler {

    @Inject MApplication application;

    public OpenNewKundeHandler() {
        super("at.ticketline.partdescriptor.newkunde", "Neuer Kunde", "Neuen Kunden anlegen");
    }

    @Override
    protected void preExecute() {
    	IEclipseContext context = application.getContext();
    	context.set("newKunde", true);
    }
}