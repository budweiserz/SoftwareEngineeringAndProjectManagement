package at.ticketline.kassa.ui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import at.ticketline.service.api.NewsService;

public class NewsPart {
	
	private Button btnWeiter;
	@Inject
	private NewsService newsService;
	
	@Inject
	MApplication application;

	private static final Logger LOG = Logger.getLogger(NewsPart.class);
	
	public NewsPart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Browser browser = new Browser(parent, SWT.NONE);
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
		
		btnWeiter.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				closeNewsAndGoToNextWindow();
				
			}
			
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@PreDestroy 
    public void preDestroy(){
    	LOG.info("News window gets closed, I will open the Main window now");
    	closeNewsAndGoToNextWindow();
    }

	private void closeNewsAndGoToNextWindow() {
		List<MWindow> windows = application.getChildren();
        Iterator<MWindow> it = windows.iterator();
        
        while(it.hasNext()) {
        	MWindow window = it.next();
        	LOG.debug(window.getElementId());
        	if(window.getElementId().equals("ticketlinercp.trimmedwindow.news")) {
        		window.setVisible(false);
        	}
        	if(window.getElementId().equals("ticketlinercp.trimmedwindow.main")) {
        		window.setVisible(true);
        		((Shell)window.getWidget()).forceFocus();
        	}
        }
	}
	
	@Focus
	public void setFocus() {
		btnWeiter.setFocus();
	}

}
