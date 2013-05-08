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


@SuppressWarnings("restriction")
public class LoginPart{
    
    private static final Logger LOG = LoggerFactory.getLogger(LoginPart.class);
    
    private Text txtUsername;
    private Text txtPassword;

    @PostConstruct
    public void createComposite(Composite parent) {
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        parent.setLayout(new RowLayout(SWT.HORIZONTAL));
        
        Label label = new Label(parent, SWT.NONE);
        label.setAlignment(SWT.CENTER);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        label.setImage(SWTResourceManager.getImage("./icons/logo.png"));
        
        Composite compContent = new Composite(parent, SWT.NONE);
        compContent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        compContent.setLayout(new GridLayout(2, false));
        
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
        
        Button btnLogin = new Button(compContent, SWT.NONE);
        btnLogin.setText("Login");
        new Label(compContent, SWT.NONE);
        
        btnLogin.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                LOG.info("Login Button pressed");
            }
        });

        
        
        Composite compFooter = new Composite(parent, SWT.NONE);
        compFooter.setLayoutData(new RowData(290, 17));
        compFooter.setLayout(new FillLayout(SWT.HORIZONTAL));
        
        Label lblVersion = new Label(compFooter, SWT.RIGHT);
        lblVersion.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        lblVersion.setAlignment(SWT.RIGHT);
        lblVersion.setText("Version 0.1");
    }

    @Focus
    public void setFocus() {
        txtUsername.setFocus();
    }
}