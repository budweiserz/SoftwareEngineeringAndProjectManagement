package at.ticketline.kassa.ui;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Mitarbeiter;
import at.ticketline.service.UserNotFoundException;
import at.ticketline.service.WrongPasswordException;
import at.ticketline.service.api.MitarbeiterService;

@SuppressWarnings("restriction")
public class LoginPart{
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginPart.class);
    
    @Inject
    private MDirtyable dirty;
    @Inject
    private EPartService partService;
    @Inject
    private EHandlerService handlerService;
    @Inject
    private ECommandService commandService;
    @Inject
    private MPart activePart;
    @Inject
    @Named(IServiceConstants.ACTIVE_SHELL)
    private Shell shell;
    
    @Inject
    MApplication application;
    
    @Inject
    private MitarbeiterService mitarbeiterService;
    
    private Text txtUsername;
    private Text txtPassword;
    private Label lblErrorMessage;
    private boolean loggedIn = false;
    
    @PostConstruct
    public void createComposite(Composite parent) {
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridLayout gl_parent = new GridLayout(1, false);
        gl_parent.marginRight = 5;
        gl_parent.marginWidth = 15;
        gl_parent.verticalSpacing = 10;
        parent.setLayout(gl_parent);
        
        Label label = new Label(parent, SWT.NONE);
        label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        label.setAlignment(SWT.CENTER);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setImage(ResourceManager.getPluginImage("at.ticketline.kassa", "icons/logo.png"));
        
        Composite compContent = new Composite(parent, SWT.NONE);
        GridData gd_compContent = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_compContent.widthHint = 292;
        compContent.setLayoutData(gd_compContent);
        compContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridLayout gl_compContent = new GridLayout(2, false);
        gl_compContent.horizontalSpacing = 10;
        compContent.setLayout(gl_compContent);
        
        Label lblUsername = new Label(compContent, SWT.NONE);
        lblUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblUsername.setText("Benutzername");
        
        txtUsername = new Text(compContent, SWT.BORDER);
        txtUsername.setToolTipText("Geben Sie hier Ihren Benutzernamen an");
        GridData gd_txtUsername = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_txtUsername.widthHint = 150;
        gd_txtUsername.minimumWidth = 150;
        txtUsername.setLayoutData(gd_txtUsername);
        
        Label lblPassword = new Label(compContent, SWT.NONE);
        lblPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblPassword.setText("Passwort");
        
        txtPassword = new Text(compContent, SWT.BORDER | SWT.PASSWORD);
        txtPassword.setToolTipText("Geben Sie hier Ihr Passwort ein");
        GridData gd_txtPassword = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_txtPassword.widthHint = 150;
        gd_txtPassword.minimumWidth = 150;
        txtPassword.setLayoutData(gd_txtPassword);
        
        Composite composite = new Composite(compContent, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
        FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
        fl_composite.marginHeight = 10;
        composite.setLayout(fl_composite);
        
        Button btnLogin = new Button(composite, SWT.NONE);
        btnLogin.setText("Login");
        
        lblErrorMessage = new Label(compContent, SWT.WRAP);
        GridData gd_lblErrorMessage = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_lblErrorMessage.widthHint = 169;
        gd_lblErrorMessage.heightHint = 50;
        lblErrorMessage.setLayoutData(gd_lblErrorMessage);
        lblErrorMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        
        btnLogin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                LOG.info("Login Attempt with User: " + txtUsername.getText());
                try {
                	Mitarbeiter m = mitarbeiterService.login(txtUsername.getText(), txtPassword.getText());
                	IEclipseContext context = application.getContext();
                	context.set("login", m);
                	if(m != null) {
                		lblErrorMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_GREEN));
                    	lblErrorMessage.setText("Login erfolgreich");
                    	closeLoginAndGoToNextWindow();
                	} else {
                		lblErrorMessage.setText("Bitte füllen Sie alle Felder aus!");
                	}
                } catch(UserNotFoundException ex) {
                	lblErrorMessage.setText("Benutzername oder Passwort falsch!");
                } catch(WrongPasswordException ex) {
                	lblErrorMessage.setText("Benutzername oder Passwort falsch!");
                } catch(Exception ex) {
                	lblErrorMessage.setText(ex.getMessage());
                }
                
            }

			private void closeLoginAndGoToNextWindow() {
				List<MWindow> windows = application.getChildren();
                Iterator<MWindow> it = windows.iterator();
                loggedIn = true;
                while(it.hasNext()) {
                	MWindow window = it.next();
                	LOG.debug(window.getElementId());
                	if(window.getElementId().equals("ticketlinercp.trimmedwindow.news")) {
                		window.setVisible(true);
                		((Shell)window.getWidget()).forceFocus();
                	}
                	if(window.getElementId().equals("ticketlinercp.trimmedwindow.login")) {
                		window.setVisible(false);
                	}
                }
			}
        });
        
        Label lblVersion = new Label(parent, SWT.SHADOW_NONE | SWT.RIGHT);
        GridData gd_lblVersion = new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1);
        gd_lblVersion.widthHint = 79;
        lblVersion.setLayoutData(gd_lblVersion);
        lblVersion.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblVersion.setAlignment(SWT.RIGHT);
        lblVersion.setText("Version 0.1");
    }

    @Focus
    public void setFocus() {
        txtUsername.setFocus();
    }
    
    @PreDestroy 
    public void preDestroy(){
    	if(!loggedIn) {
    		LOG.info("I will close the application now, Login was aborted");
    		System.exit(-1);
    	}
    }
    

}