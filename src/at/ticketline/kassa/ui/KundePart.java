package at.ticketline.kassa.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ColumnLayoutData;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.handlers.SavePartHandler;
import at.ticketline.service.api.KundeService;

@SuppressWarnings("restriction")
public class KundePart {

    private static final Logger LOG = LoggerFactory.getLogger(KundePart.class);

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

    // @Inject
    private Kunde kunde;

    @Inject
    private KundeService kundeService;

    private FormToolkit toolkit;
    private ScrolledForm form;

    private Text txtTitel;
    private Text txtVorname;
    private Text txtNachname;
    private Text txtTelefonnummer;
    private Text txtAdresse;
    private Text txtOrt;
    private Text txtEmail;
    private Text txtErmaessigung;
    private DateTime dtGeburtsdatum;

    private Button btnSave;

    /**
     * @wbp.parser.entryPoint
     */
    @Inject
    public void init(Composite parent,
            @Named(IServiceConstants.ACTIVE_SELECTION) @Optional Kunde kunde)
            throws PartInitException {
        if (kunde != null) {
            LOG.info("kunde not null");
            this.kunde = kunde;

        } else {

            kunde = new Kunde();
        }

        createControls(parent);

        if (kunde != null) {

            //setInput();
        }
    }

    private void createControls(Composite parent) {
        parent.setLayout(new GridLayout(1, false));

        this.toolkit = new FormToolkit(parent.getDisplay());
        this.form = this.toolkit.createScrolledForm(parent);
        this.form.setLayoutData(new GridData(GridData.FILL_BOTH));
        this.form.getBody().setLayout(new GridLayout(1, false));

        this.createForm(this.form.getBody());
        this.createTable(this.form.getBody());
        this.createSaveButton(this.form.getBody());
    }

    private void createForm(Composite parent) {
        Composite c = this.toolkit.createComposite(parent);
        c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        ColumnLayout columnLayout = new ColumnLayout();
        columnLayout.minNumColumns = 2;
        columnLayout.maxNumColumns = 2;
        c.setLayout(columnLayout);
        
        EditorModifyListener listener = new EditorModifyListener();

        // Left Side
        Section leftSection = this.toolkit.createSection(c, Section.EXPANDED
                | Section.TITLE_BAR);
        leftSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                KundePart.this.form.reflow(true);
            }
        });
        leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
        Composite left = this.toolkit.createComposite(leftSection);
        left.setLayout(new GridLayout(2, false));

        Label lblTitel = this.toolkit.createLabel(left, "Titel", SWT.LEFT);
        lblTitel.setSize(230, lblTitel.getSize().y);

        this.txtTitel = new Text(left, SWT.BORDER);
        txtTitel.setText("");

        this.txtTitel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.txtTitel.addModifyListener(listener);

        Label lblVorname = new Label(left, SWT.NONE);
        toolkit.adapt(lblVorname, true, true);
        lblVorname.setText("Vorname");

        this.txtVorname = new Text(left, SWT.BORDER);
        txtVorname.setText("");

        this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
                false));
        this.txtVorname.addModifyListener(listener);

        Label lblGeburtsdatum = new Label(left, SWT.NONE);
        toolkit.adapt(lblGeburtsdatum, true, true);
        lblGeburtsdatum.setText("Geburtsdatum");

        this.dtGeburtsdatum = new DateTime(left, SWT.DROP_DOWN | SWT.BORDER);
        this.dtGeburtsdatum
                .setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        //this.dtGeburtsdatum.addFocusListener(listener);
        this.toolkit.adapt(this.dtGeburtsdatum, true, true);

        Label lblAdresse = new Label(left, SWT.NONE);
        toolkit.adapt(lblAdresse, true, true);
        lblAdresse.setText("Adresse");

        txtAdresse = new Text(left, SWT.BORDER);
        txtAdresse.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                false, 1, 1));
        toolkit.adapt(txtAdresse, true, true);

        Label lblEmail = new Label(left, SWT.NONE);
        toolkit.adapt(lblEmail, true, true);
        lblEmail.setText("Email");

        leftSection.setClient(left);

        txtEmail = new Text(left, SWT.BORDER);
        txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
                1, 1));
        toolkit.adapt(txtEmail, true, true);

        // Right Side
        Section rightSection = this.toolkit.createSection(c, Section.EXPANDED
                | Section.TITLE_BAR);
        rightSection.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                KundePart.this.form.reflow(true);
            }
        });
        rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
        
                Composite right = this.toolkit.createComposite(rightSection);
                rightSection.setClient(right);
                right.setLayout(new GridLayout(2, false));
                
                        Label lblNachname = new Label(right, SWT.NONE);
                        toolkit.adapt(lblNachname, true, true);
                        lblNachname.setText("Nachname");
                        
                                txtNachname = new Text(right, SWT.BORDER);
                                txtNachname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,
                                        1, 1));
                                toolkit.adapt(txtNachname, true, true);
                                
                                        Label lblTelefonnummer = new Label(right, SWT.NONE);
                                        toolkit.adapt(lblTelefonnummer, true, true);
                                        lblTelefonnummer.setText("Telefonnummer");
                                        
                                                txtTelefonnummer = new Text(right, SWT.BORDER);
                                                txtTelefonnummer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                                        false, 1, 1));
                                                toolkit.adapt(txtTelefonnummer, true, true);
                                                
                                                        Label lblOrt = new Label(right, SWT.NONE);
                                                        toolkit.adapt(lblOrt, true, true);
                                                        lblOrt.setText("Ort");
                                                        
                                                                txtOrt = new Text(right, SWT.BORDER);
                                                                txtOrt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
                                                                        1));
                                                                toolkit.adapt(txtOrt, true, true);
                                                                
                                                                        Label lblErmigung = new Label(right, SWT.NONE);
                                                                        toolkit.adapt(lblErmigung, true, true);
                                                                        lblErmigung.setText("Ermäßigung (%)");
                                                                        
                                                                                txtErmaessigung = new Text(right, SWT.BORDER);
                                                                                txtErmaessigung.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
                                                                                        false, 1, 1));
                                                                                toolkit.adapt(txtErmaessigung, true, true);
    }

    private void createTable(Composite parent) {
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(28, 100, true));
        layout.addColumnData(new ColumnWeightData(28, 100, true));
        layout.addColumnData(new ColumnWeightData(28, 100, true));
        layout.addColumnData(new ColumnWeightData(15, 100, true));
    }

    private void createSaveButton(Composite parent) {
        this.btnSave = new Button(parent, SWT.PUSH);
        this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
                false));
        this.btnSave.setText("Speichern ");
        this.btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!KundePart.this.dirty.isDirty()) {
                    LOG.info("not dirty return");
                    return;
                }
                handlerService.activateHandler(
                        "at.ticketline.handler.savePartHandler",
                        new SavePartHandler());

                ParameterizedCommand cmd = commandService.createCommand(
                        "at.ticketline.handler.savePartHandler", null);
                try {
                    handlerService.executeHandler(cmd);
                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);
                    MessageDialog.openError(
                            KundePart.this.shell,
                            "Error",
                            "Kunde kann nicht gespeichert werden: "
                                    + ex.getMessage());
                }
            }
        });

    }
    
    /*
    private void setInput() {
        LOG.info("set input");

         * this.form.setText(this.kunde.getVorname() + " " +
         * this.kunde.getNachname());
         * 
         * if (this.kunde.getGeburtsdatum() != null) { GregorianCalendar gc =
         * this.kunde.getGeburtsdatum();
         * this.dtGeburtsdatum.setYear(gc.get(Calendar.YEAR));
         * this.dtGeburtsdatum.setMonth(gc.get(Calendar.MONTH));
         * this.dtGeburtsdatum.setDay(gc.get(Calendar.DAY_OF_MONTH)); }
    }
    */
    
    @PreDestroy
    public void dispose() {
        // nothing to do
    }

    @Focus
    public void setFocus() {
        // nothing to do
    }

    @Persist
    public void save() {

        LOG.info("Kunde speichern");

        if (this.txtTitel.getText().equals("")) {
            this.kunde.setNachname(null);
        } else {
            this.kunde.setNachname(this.txtTitel.getText());
        }
        if (this.txtVorname.getText().equals("")) {
            this.kunde.setVorname(null);
        } else {
            this.kunde.setVorname(this.txtVorname.getText());
        }
        this.kunde.setTitel(this.txtTitel.getText());

        if (this.kunde.getGeburtsdatum() == null) {
            this.kunde.setGeburtsdatum(new GregorianCalendar());
        }
        this.kunde.getGeburtsdatum().set(Calendar.YEAR,
                this.dtGeburtsdatum.getYear());
        this.kunde.getGeburtsdatum().set(Calendar.MONTH,
                this.dtGeburtsdatum.getMonth());
        this.kunde.getGeburtsdatum().set(Calendar.DAY_OF_MONTH,
                this.dtGeburtsdatum.getDay());

        try {
            this.kundeService.save(kunde);
            this.dirty.setDirty(false);

            MessageDialog.openInformation(this.shell, "Speichervorgang",
                    "Kunde wurde erfolgreich gespeichert");
        } catch (ConstraintViolationException c) {
            StringBuilder sb = new StringBuilder(
                    "Die eingegebene Daten weisen folgende Fehler auf:\n");
            for (ConstraintViolation<?> cv : c.getConstraintViolations()) {
                sb.append(cv.getPropertyPath().toString().toUpperCase())
                        .append(" ").append(cv.getMessage() + "\n");
            }
            MessageDialog.openError(this.shell, "Error", sb.toString());

        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            MessageDialog.openError(
                    this.shell,
                    "Error",
                    "Künstler konnte nicht gespeichert werden: "
                            + e.getMessage());
        }
    }

    private void updateTitle() {
        partService.getActivePart().setLabel(
                this.txtVorname.getText() + " " + this.txtTitel.getText());
    }
    
    class EditorModifyListener implements ModifyListener, FocusListener {

        @Override
        public void modifyText(ModifyEvent e) {
            if ((e.getSource().equals(KundePart.this.txtNachname))
                    || (e.getSource().equals(KundePart.this.txtVorname))) {
                KundePart.this.updateTitle();
            }
            dirty.setDirty(true);
        }

        @Override
        public void focusGained(FocusEvent e) {
            // nothing to do
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource().equals(KundePart.this.dtGeburtsdatum) == false) {
                return;
            }
            try {
                GregorianCalendar gc = KundePart.this.kunde
                        .getGeburtsdatum();
                if (gc == null) {
                    dirty.setDirty(true);
                    return;
                }
                if ((KundePart.this.dtGeburtsdatum.getYear() != gc
                        .get(Calendar.YEAR))
                        || (KundePart.this.dtGeburtsdatum.getMonth() != gc
                                .get(Calendar.MONTH))
                        || (KundePart.this.dtGeburtsdatum.getDay() != gc
                                .get(Calendar.DAY_OF_MONTH))) {
                    dirty.setDirty(true);
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }
        }
    }
}