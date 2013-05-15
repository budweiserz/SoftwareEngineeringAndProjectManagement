package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import at.ticketline.service.api.NewsService;

public class NewsPart {
	
	private Button btnWeiter;
	@Inject
	private NewsService newsService;

	public NewsPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Browser browser = new Browser(parent, SWT.FILL | SWT.SMOOTH);
		GridData gd_browser = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_browser.heightHint = 172;
		gd_browser.widthHint = 417;
		browser.setLayoutData(gd_browser);
		browser.setText(new NewsHTMLGeneratorImpl().generateHTMLFromNewsItems(newsService.findAll()));
		
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setLayout(new GridLayout(13, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true, false, 1, 1));
		composite.setSize(100, 40);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		btnWeiter = new Button(composite, SWT.NONE);
		btnWeiter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		btnWeiter.setText("Weiter");
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		btnWeiter.setFocus();
	}

}
