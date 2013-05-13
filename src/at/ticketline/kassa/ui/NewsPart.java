package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;

public class NewsPart {

	public NewsPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Browser browser = new Browser(parent, SWT.FILL);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_browser.widthHint = 298;
		browser.setLayoutData(gd_browser);
		
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, false, false, 1, 1));
		composite.setSize(100, 40);
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}

}
