package at.ticketline.kassa.handlers;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for handler classes that open the part in a new tab.
 * 
 * Usage:
 *  
 * public class OpenKuenstlerSearchHandler extends NewTabHandler {
 *     public OpenKuenstlerSearchHandler() {
 *         super("at.ticketline.partdescriptor.searchKuenstler", 
 *               "Suche Künstler", "Eine Suche für einen Künstler starten");
 *     }
 * }
 * 
 * @author Florian Klampfer
 */
@SuppressWarnings("restriction")
public abstract class NewTabHandler {
    private static final Logger LOG = LoggerFactory.getLogger(NewTabHandler.class);
    
    private String partName;
    private String label;
    private String tooltip;
    
    /**
     * @param partName full qualified part descriptor id, like "at.ticketline.partdescriptor.searchVeranstaltungsort"
     */
    public NewTabHandler(String partName) {
        this.partName = partName;
    }
    
    /**
     * 
     * @param partName full qualified part descriptor id, like "at.ticketline.partdescriptor.searchVeranstaltungsort"
     * @param label Label of the tab
     * @param tooltip Tooltip of the tab
     */
    public NewTabHandler(String partName, String label, String tooltip) {
        this(partName);
        this.label = label;
        this.tooltip = tooltip;
    }

    /**
     * Oeffnet neuen leeren KuenstlerPart im PartStack "at.ticketline.kassa.partstack"
     * @param application
     * @param modelService
     * @param partService
     * @param context
     * @throws ExecutionException
     */
    @Execute
    public void execute(MApplication application, EModelService modelService, EPartService partService, IEclipseContext context) throws ExecutionException {
        preExecute();
        openPart(application, modelService, partService, context);
    }
    
    protected void preExecute() {
        
    }

    private void openPart(MApplication application, EModelService modelService, EPartService partService, IEclipseContext context) throws ExecutionException {
    
        // Öffene PartStack (von Applikation Model)
        MPartStack stack = (MPartStack) modelService.find("at.ticketline.kassa.partstack.main", application);
    
        // Erstelle Part (basierend auf einen PartDescriptor im Application Model)
        MPart part = partService.createPart(partName);
        
        // Allgemeine Einstellungen des Parts
        part.setLabel(label);
        part.setTooltip(tooltip);
        part.setCloseable(true);
        
        /**
         * XXX: Prevent cretain tabs from open multiple times
         */
        for(MStackElement mse : stack.getChildren()) {
            LOG.debug(mse.toString());
            if (!this.canOpenMultiple() && mse.getElementId().equals(part.getElementId())) {
                partService.showPart((MPart)mse, PartState.ACTIVATE);
                return;
            }
        }
        
        stack.getChildren().add(part);

        // Part anzeigen
        try {
            partService.showPart(part, PartState.ACTIVATE);
        } catch (Exception e) {
            LOG.error("Part {} konnte nicht geoeffnet werden: {}", partName, e.getMessage());
        }
    }
    
    /**
     * Override in sub classes.
     * @return true if the tab can open multiple times.
     */
    protected boolean canOpenMultiple() {
        return true;
    }
}
