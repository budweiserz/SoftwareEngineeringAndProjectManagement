package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;

public class InvisibleMainPart {

	
	private static final Logger LOG = Logger.getLogger(InvisibleMainPart.class);
	
	public InvisibleMainPart() {
		LOG.info("InvisibleMainPart wurde initialisiert");
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		LOG.info("InvisibleMainPart wurde initialisiert- Postconstruct");

		
		parent.setLayout(new GridLayout(1, false));
		
		Label lblItIsA = new Label(parent, SWT.NONE);
		lblItIsA.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
		lblItIsA.setText("It is a Test!");
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
