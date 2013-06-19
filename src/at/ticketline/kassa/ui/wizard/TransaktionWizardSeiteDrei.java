package at.ticketline.kassa.ui.wizard;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.validation.ConstraintViolationException;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.handlers.SavePartHandler;
import at.ticketline.kassa.ui.UIUtilities;
import at.ticketline.service.api.KundeService;

@SuppressWarnings("restriction")
public class TransaktionWizardSeiteDrei extends WizardPage {

    private static final Logger LOG = LoggerFactory.getLogger(TransaktionWizardSeiteDrei.class);

    
    private MDirtyable dirty;
    private EHandlerService handlerService;
    @SuppressWarnings("unused")
    private ECommandService commandService;
    @Named(IServiceConstants.ACTIVE_SHELL)
    private Shell shell;
    private KundeService kundeService;
    private Kunde kunde;
    
    private FormToolkit toolkit;
    private ScrolledForm form;

    private Text txtTitel;
    private Text txtVorname;
    private Text txtNachname;
    private DateTime dtGeburtsdatum;
    private Text txtTelefon;
    private Text txtAdresse;
    private Text txtOrt;
    private Text txtEmail;
    private Combo cbGeschlecht;

    private Button btnSave;
    
    private TransaktionWizardValues values;
    
    /**
     * Diese Wizard Seite wird aufgerufen, wenn ein neuer Kunde
     * anzulegen ist. Es wird das Formular von KundePart angezeigt.
     * 
     */
    public TransaktionWizardSeiteDrei(TransaktionWizardValues values) {
        super("NeuerKunde");
        setTitle("Neuer Kunde");
        setDescription("Tragen sie die Informationen des Kunden ein:");
        kunde = new Kunde();
        this.values = values;
    }

    /**
     * Erstelle die UI Inhalte dieser Seite.
     */
    @Override
    public void createControl(Composite parent) {
        LOG.info("Erstelle Wizard Seite für neuen Kunden...");
        Composite container = new Composite(parent, SWT.NULL);
        createNewKundeForm(container);
        setControl(container);
        setPageComplete(false);
    }
    
    private void createNewKundeForm(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        this.toolkit = new FormToolkit(parent.getDisplay());
        this.form = this.toolkit.createScrolledForm(parent);
        this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.form.getBody().setLayout(new GridLayout(1, false));

        this.createForm(this.form.getBody());
        this.createSaveButton(this.form.getBody());
    }
    
    private void createForm(Composite parent) {
        Composite c = this.toolkit.createComposite(parent);
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ColumnLayout columnLayout = new ColumnLayout();
        columnLayout.minNumColumns = 1;
        columnLayout.maxNumColumns = 1;
        c.setLayout(columnLayout);

        EditorModifyListener listener = new EditorModifyListener();


        Composite left = this.toolkit.createComposite(parent);
        left.setLayout(new GridLayout(4, false));

        this.toolkit.createLabel(left, "Titel:", SWT.LEFT);
        this.txtTitel = this.toolkit.createText(left, this.kunde.getTitel(), SWT.LEFT | SWT.BORDER);
        this.txtTitel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtTitel.addModifyListener(listener);
        new Label(left, SWT.NONE);
        new Label(left, SWT.NONE);

        this.toolkit.createLabel(left, "Vorname:", SWT.LEFT);
        this.txtVorname = this.toolkit.createText(left, this.kunde.getVorname(), SWT.LEFT | SWT.BORDER);
        this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtVorname.addModifyListener(listener);

        this.toolkit.createLabel(left, "Nachname:", SWT.LEFT);
        this.txtNachname = this.toolkit.createText(left, this.kunde.getNachname(), SWT.LEFT | SWT.BORDER);
        this.txtNachname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtNachname.addModifyListener(listener);

        this.toolkit.createLabel(left, "Adresse:", SWT.LEFT);
        this.txtAdresse = this.toolkit.createText(left, "", SWT.LEFT | SWT.BORDER);
        if (kunde.getAdresse() != null) {
            txtAdresse.setText(kunde.getAdresse().getStrasse());
        }
        this.txtAdresse.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtAdresse.addModifyListener(listener);

        this.toolkit.createLabel(left, "Ort:", SWT.LEFT);
        this.txtOrt = this.toolkit.createText(left, "", SWT.LEFT | SWT.BORDER);
        if (kunde.getAdresse() != null) {
            txtOrt.setText(kunde.getAdresse().getOrt());
        }
        this.txtOrt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtOrt.addModifyListener(listener);

        this.toolkit.createLabel(left, "Geburtsdatum:", SWT.LEFT);
        this.dtGeburtsdatum = new DateTime(left, SWT.DROP_DOWN | SWT.BORDER);
        this.dtGeburtsdatum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.dtGeburtsdatum.addFocusListener(listener);

        this.toolkit.createLabel(left, "Geschlecht:", SWT.LEFT);
        this.cbGeschlecht = new Combo(left, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER);
        this.cbGeschlecht.setItems(Geschlecht.toStringArray());
        this.cbGeschlecht.select(0);
        this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.cbGeschlecht.addModifyListener(listener);

        this.toolkit.createLabel(left, "Telefonnummer:", SWT.LEFT);
        this.txtTelefon = this.toolkit.createText(left, this.kunde.getTelnr(), SWT.LEFT | SWT.BORDER);
        this.txtTelefon.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtTelefon.addModifyListener(listener);

        this.toolkit.createLabel(left, "Email:", SWT.LEFT);
        this.txtEmail = this.toolkit.createText(left, this.kunde.getEmail(), SWT.LEFT | SWT.BORDER);
        this.txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtEmail.addModifyListener(listener);

    }

    protected boolean saveNewKunde() {
        if (!TransaktionWizardSeiteDrei.this.dirty.isDirty()) {
            return false;
        }
        handlerService.activateHandler("at.ticketline.handler.savePartHandler", new SavePartHandler());

        //ParameterizedCommand cmd = commandService.createCommand("at.ticketline.handler.savePartHandler", null);
        //Kunde k = new Kunde();
        try {
            boolean result = save();
            return result;
            //handlerService.executeHandler(cmd);
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            MessageDialog.openError(TransaktionWizardSeiteDrei.this.shell, "Error", "Kunde kann nicht gespeichert werden: "
                    + ex.getMessage());
            return false;
        }
    }
    
    private void createSaveButton(Composite parent) {

        this.btnSave = new Button(parent, SWT.PUSH);
        this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
        this.btnSave.setText("Speichern ");
        this.btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!TransaktionWizardSeiteDrei.this.dirty.isDirty()) {
                    return;
                }
                handlerService.activateHandler("at.ticketline.handler.savePartHandler", new SavePartHandler());

                //ParameterizedCommand cmd = commandService.createCommand("at.ticketline.handler.savePartHandler", null);
                //Kunde k = new Kunde();
                try {
                    save();
                    //handlerService.executeHandler(cmd);
                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);
                    MessageDialog.openError(TransaktionWizardSeiteDrei.this.shell, "Error", "Kunde kann nicht gespeichert werden: "
                            + ex.getMessage());
                }
            }
        });
    }
    

    @Override
    @PreDestroy
    public void dispose() {
        // nothing to do
    }

    @Persist
    public boolean save() {
        LOG.info("Kunde speichern");

        Adresse a = new Adresse();

        this.kunde.setTitel(this.txtVorname.getText());

        if (this.txtVorname.getText().equals("")) {
            this.kunde.setVorname(null);
        } else {
            this.kunde.setVorname(this.txtVorname.getText());
        }

        if (this.txtNachname.getText().equals("")) {
            this.kunde.setNachname(null);
        } else {
            this.kunde.setNachname(this.txtNachname.getText());
        }

        if (this.txtAdresse.getText().equals("")) {
            this.kunde.setAdresse(null);
        } else {
            a.setStrasse(txtAdresse.getText());
            this.kunde.setAdresse(a);
        }

        if (this.txtOrt.getText().equals("")) {
            this.kunde.setAdresse(null);
        } else {
            a.setOrt(txtOrt.getText());
        }

        if (this.txtTelefon.getText().equals("")) {
            this.kunde.setTelnr(null);
        } else {
            this.kunde.setTelnr(txtTelefon.getText());
        }
        
        if (this.txtEmail.getText().equals("")) {
            this.kunde.setEmail(null);
        } else {
            this.kunde.setEmail(txtEmail.getText());
        }
        
        this.kunde.setGeschlecht(Geschlecht.getValueOf(this.cbGeschlecht.getText()));
        if (this.kunde.getGeburtsdatum() == null) {
            this.kunde.setGeburtsdatum(new GregorianCalendar());
        }
        
        this.kunde.getGeburtsdatum().set(Calendar.YEAR, this.dtGeburtsdatum.getYear());
        this.kunde.getGeburtsdatum().set(Calendar.MONTH, this.dtGeburtsdatum.getMonth());
        this.kunde.getGeburtsdatum().set(Calendar.DAY_OF_MONTH, this.dtGeburtsdatum.getDay());
        
        try {
            this.kundeService.save(kunde);
            this.values.setKunde(kunde);
            ((TransaktionWizard)getWizard()).fuenf.updateContent();
            this.dirty.setDirty(false);
            MessageDialog.openInformation(this.shell, "Speichervorgang", "Kunde wurde erfolgreich gespeichert");
            setPageComplete(true);
            this.btnSave.setEnabled(false);
            return true;
        } catch (ConstraintViolationException c) {

            MessageDialog.openError(this.shell, "Error", UIUtilities.getReadableConstraintViolations(c));
            return false;

        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            MessageDialog.openError(this.shell, "Error", "Kunde konnte nicht gespeichert werden: " + e.getMessage());
            return false;
        }
    }

    class EditorModifyListener implements ModifyListener, FocusListener {

        @Override
        public void modifyText(ModifyEvent e) {
            dirty.setDirty(true);
        }

        @Override
        public void focusGained(FocusEvent e) {
            // nothing to do
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource().equals(TransaktionWizardSeiteDrei.this.dtGeburtsdatum) == false) {
                return;
            }
            try {
                GregorianCalendar gc = TransaktionWizardSeiteDrei.this.kunde.getGeburtsdatum();
                if (gc == null) {
                    dirty.setDirty(true);
                    return;
                }
                if ((TransaktionWizardSeiteDrei.this.dtGeburtsdatum.getYear() != gc.get(Calendar.YEAR))
                        || (TransaktionWizardSeiteDrei.this.dtGeburtsdatum.getMonth() != gc.get(Calendar.MONTH))
                        || (TransaktionWizardSeiteDrei.this.dtGeburtsdatum.getDay() != gc.get(Calendar.DAY_OF_MONTH))) {
                    dirty.setDirty(true);
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }

        }
    }
    
    
    @Override
    public WizardPage getNextPage(){
       return ((TransaktionWizard)getWizard()).fuenf;
     }

    public void setDirty(MDirtyable dirty) {
        this.dirty = dirty;
    }

    public void setHandlerService(EHandlerService handlerService) {
        this.handlerService = handlerService;
    }

    public void setCommandService(ECommandService commandService) {
        this.commandService = commandService;
    }

    public void setShell(Shell shell) {
        this.shell = shell;
    }

    public void setKundeService(KundeService kundeService) {
        this.kundeService = kundeService;
    }

    @Focus
    public void setFocus() {
        btnSave.setFocus();
    }
}
