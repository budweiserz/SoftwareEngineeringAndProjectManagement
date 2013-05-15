
package at.ticketline.kassa.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("restriction")
public class OpenVeranstaltungsSucheHandler {
	
	private static final Logger LOG = LoggerFactory.getLogger(OpenKuenstlerHandler.class);

	
	@Execute
	public void execute(
			MApplication application,
			EModelService modelService,
			EPartService partService,
			IEclipseContext context
			) throws ExecutionException {
		openPart(application, modelService, partService, context);
	}

	private void openPart(MApplication application,
			EModelService modelService,
			EPartService partService,
			IEclipseContext context
			) throws ExecutionException {

		// Öffene PartStack (von Applikation Model)
		MPartStack stack = (MPartStack) modelService.find("at.ticketline.kassa.partstack.main", application);

		// Erstelle Part (basierend auf einen PartDescriptor im Application Model)
		MPart part = partService.createPart("at.ticketline.partdescriptor.searchVeranstaltung");

		// Allgemeine Einstellungen des Parts
		part.setLabel("Suche Veranstaltung");
		part.setTooltip("Eine Suche für eine Veranstaltung starten");
		part.setCloseable(true);

		stack.getChildren().add(part);

		// Part anzeigen
		try {
			partService.showPart(part, PartState.ACTIVATE);
		} catch (Exception e) {
			LOG.error("Part fuer neuen Veranstaltungssuche konnte nicht geoeffnet werden: {}", e.getMessage());
		}
	}

}