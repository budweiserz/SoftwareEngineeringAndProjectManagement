package at.ticketline.kassa.ui;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
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
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.kassa.handlers.SavePartHandler;
import at.ticketline.service.api.KundeService;
import org.eclipse.swt.widgets.Spinner;

@SuppressWarnings("restriction")
public class KundePart {

    private static final Logger LOG = LoggerFactory.getLogger(KundePart.class);

    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private MPart activePart;
    @Inject @Named(IServiceConstants.ACTIVE_SHELL) private Shell shell;

    @Inject private Kunde kunde;
    @Inject private KundeService kundeService;

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
    private Section section_extended;
    private Spinner spinner;

    protected boolean created = false;

    /**
     * @wbp.parser.entryPoint
     */
    @Inject
    public void init(Composite parent, @Named(IServiceConstants.ACTIVE_SELECTION) @Optional Kunde kunde) throws PartInitException {
        overrideableInit(parent, kunde);
    }

    protected void overrideableInit(Composite parent, Kunde kunde) {
        /*
         * XXX: When multiple Tabs are open Eclipse will show the first x 
         * Kuenstler in the first tab. Then the first x-1 Kuenstler in the 
         * second tab and so on.
         * Having a created boolean fixes this.
         */
        if(created == false) {
            created = true;
            if (kunde != null) {
                this.kunde = kunde;
            }

            createControls(parent);
    
            if (kunde != null) {
                setInput();
            } else {
                this.activePart.setLabel("Neuer Kunde");
            }
        }
    }

    @PostConstruct
    private void initTitle() {
        LOG.debug("post construct kunde");
        this.updateTitle();
    }

    protected void createControls(Composite parent) {
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
        GridData gd_c = new GridData(GridData.FILL_HORIZONTAL);
        gd_c.heightHint = 400;
        c.setLayoutData(gd_c);

        ColumnLayout columnLayout = new ColumnLayout();
        columnLayout.minNumColumns = 1;
        columnLayout.maxNumColumns = 1;
        c.setLayout(columnLayout);

        EditorModifyListener listener = new EditorModifyListener();

        Section section = this.toolkit.createSection(c, Section.EXPANDED | Section.DESCRIPTION | Section.TWISTIE | Section.TITLE_BAR);
        section.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                KundePart.this.form.reflow(true);
            }
        });

        section.setText("Daten");
        section.setLayoutData(new ColumnLayoutData());
        Composite composite = this.toolkit.createComposite(section);
        composite.setLayout(new GridLayout(4, false));

        this.toolkit.createLabel(composite, "Titel:", SWT.LEFT);
        this.txtTitel = this.toolkit.createText(composite, this.kunde.getTitel(), SWT.LEFT | SWT.BORDER);
        this.txtTitel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtTitel.addModifyListener(listener);
        new Label(composite, SWT.NONE);
        new Label(composite, SWT.NONE);

        this.toolkit.createLabel(composite, "Vorname*:", SWT.LEFT);
        this.txtVorname = this.toolkit.createText(composite, this.kunde.getVorname(), SWT.LEFT | SWT.BORDER);
        this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtVorname.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Nachname*:", SWT.LEFT);
        this.txtNachname = this.toolkit.createText(composite, this.kunde.getNachname(), SWT.LEFT | SWT.BORDER);
        this.txtNachname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtNachname.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Adresse:", SWT.LEFT);
        this.txtAdresse = this.toolkit.createText(composite, "", SWT.LEFT | SWT.BORDER);
        if (kunde.getAdresse() != null) {
            txtAdresse.setText(kunde.getAdresse().getStrasse());
        }
        this.txtAdresse.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtAdresse.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Ort:", SWT.LEFT);
        this.txtOrt = this.toolkit.createText(composite, "", SWT.LEFT | SWT.BORDER);
        if (kunde.getAdresse() != null) {
            txtOrt.setText(kunde.getAdresse().getOrt());
        }
        this.txtOrt.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtOrt.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Geburtsdatum:", SWT.LEFT);
        this.dtGeburtsdatum = new DateTime(composite, SWT.DROP_DOWN | SWT.BORDER);
        this.dtGeburtsdatum.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.dtGeburtsdatum.addFocusListener(listener);

        this.toolkit.createLabel(composite, "Geschlecht:", SWT.LEFT);
        this.cbGeschlecht = new Combo(composite, SWT.FLAT | SWT.READ_ONLY | SWT.BORDER);
        this.cbGeschlecht.setItems(Geschlecht.toStringArray());
        this.cbGeschlecht.select(0);
        this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        this.cbGeschlecht.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Telefonnummer:", SWT.LEFT);
        this.txtTelefon = this.toolkit.createText(composite, this.kunde.getTelnr(), SWT.LEFT | SWT.BORDER);
        this.txtTelefon.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtTelefon.addModifyListener(listener);

        this.toolkit.createLabel(composite, "Email:", SWT.LEFT);
        this.txtEmail = this.toolkit.createText(composite, this.kunde.getEmail(), SWT.LEFT | SWT.BORDER);
        this.txtEmail.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        this.txtEmail.addModifyListener(listener);

        section.setClient(composite);
        
        section_extended = toolkit.createSection(c, Section.DESCRIPTION | Section.TWISTIE);
        section_extended.setText("Erweitert");
        section_extended.setExpanded(false);
        section_extended.setLayoutData(new ColumnLayoutData());
        Composite composite_extended = this.toolkit.createComposite(section_extended);
        composite_extended.setLayout(new GridLayout(4, false));
        
        this.toolkit.createLabel(composite_extended, "Bonuspunkte:", SWT.LEFT);
        
        spinner = new Spinner(composite_extended, SWT.BORDER);
        toolkit.adapt(spinner);
        toolkit.paintBordersFor(spinner);
        this.spinner.addModifyListener(listener);
        
        section_extended.setClient(composite_extended);
    }

    private void createSaveButton(Composite parent) {

        this.btnSave = new Button(parent, SWT.PUSH);
        this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));
        this.btnSave.setText("Speichern ");
        this.btnSave.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!KundePart.this.dirty.isDirty()) {
                    return;
                }
                handlerService.activateHandler("at.ticketline.handler.savePartHandler", new SavePartHandler());

                ParameterizedCommand cmd = commandService.createCommand("at.ticketline.handler.savePartHandler", null);
                try {
                    handlerService.executeHandler(cmd);
                } catch (Exception ex) {
                    LOG.error(ex.getMessage(), ex);
                    MessageDialog.openError(KundePart.this.shell, "Error", "Kunde kann nicht gespeichert werden: "
                            + ex.getMessage());
                }
            }
        });
    }

    private void setInput() {

        this.form.setText(kunde.getVorname() + " " + kunde.getNachname());

        if (this.kunde.getGeschlecht() == Geschlecht.WEIBLICH) {
            this.cbGeschlecht.select(1);
        } else {
            this.cbGeschlecht.select(0);
        }

        if (this.kunde.getGeburtsdatum() != null) {
            GregorianCalendar gc = this.kunde.getGeburtsdatum();
            this.dtGeburtsdatum.setYear(gc.get(Calendar.YEAR));
            this.dtGeburtsdatum.setMonth(gc.get(Calendar.MONTH));
            this.dtGeburtsdatum.setDay(gc.get(Calendar.DAY_OF_MONTH));
        }
        if(this.kunde.getPunkte() != null) {
            spinner.setSelection(this.kunde.getPunkte().intValue());
        }
        dirty.setDirty(false);
    }

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

        Adresse a = new Adresse();

        this.kunde.setTitel(this.txtTitel.getText());

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
        
        this.kunde.setPunkte(new BigDecimal(this.spinner.getSelection()));
        
        try {
            this.kundeService.save(kunde);
            this.dirty.setDirty(false);

            MessageDialog.openInformation(this.shell, "Speichervorgang", "Kunde wurde erfolgreich gespeichert");
        } catch (ConstraintViolationException c) {

            MessageDialog.openError(this.shell, "Error", UIUtilities.getReadableConstraintViolations(c));

        } catch (DaoException e) {
            LOG.error(e.getMessage(), e);
            MessageDialog.openError(this.shell, "Error", "Kunde konnte nicht gespeichert werden: " + e.getMessage());
        }
    }

    private void updateTitle() {
        activePart.setLabel(this.txtVorname.getText() + " " + this.txtNachname.getText());
    }

    class EditorModifyListener implements ModifyListener, FocusListener {

        @Override
        public void modifyText(ModifyEvent e) {
            if ((e.getSource().equals(KundePart.this.txtNachname)) || (e.getSource().equals(KundePart.this.txtVorname))) {
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
                GregorianCalendar gc = KundePart.this.kunde.getGeburtsdatum();
                if (gc == null) {
                    dirty.setDirty(true);
                    return;
                }
                if ((KundePart.this.dtGeburtsdatum.getYear() != gc.get(Calendar.YEAR))
                        || (KundePart.this.dtGeburtsdatum.getMonth() != gc.get(Calendar.MONTH))
                        || (KundePart.this.dtGeburtsdatum.getDay() != gc.get(Calendar.DAY_OF_MONTH))) {
                    dirty.setDirty(true);
                }
            } catch (Exception ex) {
                LOG.error(ex.getMessage(), ex);
            }

        }
    }
}