package at.ticketline.kassa.handlers;

import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

@SuppressWarnings("restriction")
public class closeLogin {
	
	private static final Logger LOG = Logger.getLogger(closeLogin.class);
	
    @Execute
    public void execute(MApplication application, EModelService modelService) {
    	MWindow m = null;
    	List<MTrimmedWindow> windows = modelService.findElements(application, null,
                MTrimmedWindow.class, null);
            if (windows.size() >= 1) {
              m = windows.get(0);
            }
    	MWindow e = MBasicFactory.INSTANCE.createTrimmedWindow();
    	e.setWidth(200);
        e.setHeight(200);
        LOG.info("Checkpoint 1");
        LOG.info(m.getElementId());
        application.getChildren().add(e);
        application.getChildren().add(m);
    }
}
