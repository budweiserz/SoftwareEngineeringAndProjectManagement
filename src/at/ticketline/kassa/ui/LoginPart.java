package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.kassa.handlers.SavePartHandler;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.custom.StackLayout;

@SuppressWarnings("restriction")
public class LoginPart{
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginPart.class);
    
    private Text txtUsername;
    private Text txtPassword;

    @PostConstruct
    public void createComposite(Composite parent) {
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridLayout gl_parent = new GridLayout(1, false);
        gl_parent.marginWidth = 15;
        gl_parent.verticalSpacing = 15;
        parent.setLayout(gl_parent);
        
        Label label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setImage(ResourceManager.getPluginImage("at.ticketline.kassa", "icons/logo.png"));
        
        Composite compContent = new Composite(parent, SWT.NONE);
        compContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        GridLayout gl_compContent = new GridLayout(2, false);
        compContent.setLayout(gl_compContent);
        
        Label lblUsername = new Label(compContent, SWT.NONE);
        lblUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblUsername.setText("Username");
        
        txtUsername = new Text(compContent, SWT.BORDER);
        txtUsername.setToolTipText("Geben Sie hier Ihren Usernamen an");
        GridData gd_txtUsername = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_txtUsername.minimumWidth = 200;
        txtUsername.setLayoutData(gd_txtUsername);
        
        Label lblPassword = new Label(compContent, SWT.NONE);
        lblPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblPassword.setText("Passwort");
        
        txtPassword = new Text(compContent, SWT.BORDER | SWT.PASSWORD);
        txtPassword.setToolTipText("Geben Sie hier Ihr Passwort ein");
        GridData gd_txtPassword = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
        gd_txtPassword.minimumWidth = 200;
        txtPassword.setLayoutData(gd_txtPassword);
        
        Composite composite = new Composite(compContent, SWT.NONE);
        FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
        fl_composite.marginHeight = 10;
        composite.setLayout(fl_composite);
        
        Button btnLogin = new Button(composite, SWT.NONE);
        btnLogin.setText("Login");
        new Label(compContent, SWT.NONE);
        
        btnLogin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                LOG.info("Login Button pressed");
            }
        });
        
        Label lblVersion = new Label(parent, SWT.SHADOW_NONE | SWT.RIGHT);
        lblVersion.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblVersion.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblVersion.setAlignment(SWT.RIGHT);
        lblVersion.setText("Version 0.1");

        
        
        Composite compFooter = new Composite(parent, SWT.NONE);
        compFooter.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
        compFooter.setLayout(new FillLayout(SWT.HORIZONTAL));
    }

    @Focus
    public void setFocus() {
        txtUsername.setFocus();
    }
}