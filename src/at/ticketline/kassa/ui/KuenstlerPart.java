package at.ticketline.kassa.ui;

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
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
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
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.kassa.handlers.SavePartHandler;
import at.ticketline.service.api.KuenstlerService;

@SuppressWarnings("restriction")
public class KuenstlerPart{
	
	private static final Logger LOG = LoggerFactory.getLogger(KuenstlerPart.class);
	
	@Inject private MDirtyable dirty;
	@Inject private EPartService partService;
	@Inject private EHandlerService handlerService;
	@Inject private ECommandService commandService;
	@Inject private MPart activePart;
	@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
	
	@Inject private Kuenstler kuenstler;
	@Inject private KuenstlerService kuenstlerService;
	
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private Text txtNachname;
	private Text txtVorname;
	private Text txtTitel;
	private Combo cbGeschlecht;
	private Text txtBiographie;
	private DateTime dtGeburtsdatum;

	private Button btnSave;

	private TableViewer tableViewer;

	@Inject
	public void init(Composite parent,
					@Named (IServiceConstants.ACTIVE_SELECTION) @Optional Kuenstler kuenstler) throws PartInitException {
		if(kuenstler != null){
			this.kuenstler = kuenstler;
		}
		createControls(parent);
		
		if(kuenstler != null){
			setInput();
		}
	}

	private void createControls(Composite parent){
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
		Section leftSection = this.toolkit.createSection(c, Section.DESCRIPTION
				| Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		leftSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				KuenstlerPart.this.form.reflow(true);
			}
		});
		leftSection.setText("Daten");
		leftSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
		Composite left = this.toolkit.createComposite(leftSection);
		left.setLayout(new GridLayout(2, false));

		Label lblNachname = this.toolkit.createLabel(left, "Nachname:",
				SWT.LEFT);
		lblNachname.setSize(230, lblNachname.getSize().y);

		this.txtNachname = this.toolkit.createText(left, this.kuenstler
				.getNachname(), SWT.LEFT | SWT.BORDER);
		
		this.txtNachname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.txtNachname.addModifyListener(listener);

		this.toolkit.createLabel(left, "Vorname:", SWT.LEFT);

		this.txtVorname = this.toolkit.createText(left, this.kuenstler
				.getVorname(), SWT.LEFT | SWT.BORDER);
		
		this.txtVorname.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false));
		this.txtVorname.addModifyListener(listener);

		this.toolkit.createLabel(left, "Titel:", SWT.LEFT);

		this.txtTitel = this.toolkit.createText(left,
				this.kuenstler.getTitel(), SWT.LEFT | SWT.BORDER);
		
		this.txtTitel
				.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		this.txtTitel.addModifyListener(listener);

		this.toolkit.createLabel(left, "Geschlecht:",
				SWT.LEFT);

		this.cbGeschlecht = new Combo(left, SWT.FLAT | SWT.READ_ONLY
				| SWT.BORDER);
		this.cbGeschlecht.setItems(Geschlecht.toStringArray());
		
		this.cbGeschlecht.select(0);
		
		this.cbGeschlecht.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.cbGeschlecht.addModifyListener(listener);
		this.toolkit.adapt(this.cbGeschlecht, true, true);

		this.toolkit.createLabel(left, "Geburtsdatum:",
				SWT.LEFT);

		this.dtGeburtsdatum = new DateTime(left, SWT.DROP_DOWN | SWT.BORDER);
		this.dtGeburtsdatum
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		this.dtGeburtsdatum.addFocusListener(listener);
		this.toolkit.adapt(this.dtGeburtsdatum, true, true);

		leftSection.setClient(left);

		// Right Side
		Section rightSection = this.toolkit.createSection(c,
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		rightSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				KuenstlerPart.this.form.reflow(true);
			}
		});
		rightSection.setText("Biographie");
		rightSection.setLayoutData(new ColumnLayoutData(ColumnLayoutData.FILL));
		
		Composite right = this.toolkit.createComposite(rightSection);
		right.setLayout(new GridLayout(1, false));

		this.txtBiographie = this.toolkit.createText(right, this.kuenstler
				.getBiographie(), SWT.MULTI | SWT.BORDER | SWT.WRAP);
		
		this.txtBiographie.setLayoutData(new GridData(GridData.FILL_BOTH));
		this.txtBiographie.addModifyListener(listener);
		rightSection.setClient(right);
	}

	private void createTable(Composite parent) {
		Section engagementSection = this.toolkit.createSection(parent,
				Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE
						| Section.EXPANDED);
		engagementSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				KuenstlerPart.this.form.reflow(true);
			}
		});
		engagementSection.setText("Engagements");
		engagementSection.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.tableViewer = new TableViewer(engagementSection, SWT.BORDER
				| SWT.FULL_SELECTION);
		this.tableViewer.getTable().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(28, 100, true));
		layout.addColumnData(new ColumnWeightData(28, 100, true));
		layout.addColumnData(new ColumnWeightData(28, 100, true));
		layout.addColumnData(new ColumnWeightData(15, 100, true));
		this.tableViewer.getTable().setLayout(layout);

		this.tableViewer.getTable().setLinesVisible(true);
		this.tableViewer.getTable().setHeaderVisible(true);

		this.tableViewer.setContentProvider(new ArrayContentProvider());
		this.tableViewer.setLabelProvider(new ITableLabelProvider() {

			@Override
			public Image getColumnImage(Object arg0, int arg1) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int index) {
				Engagement e = (Engagement) element;
				switch (index) {
				case 0:
					if (e.getVeranstaltung() != null) {
						return e.getVeranstaltung().getBezeichnung();
					} else {
						return "";
					}
				case 1:
					if (e.getVeranstaltung() != null) {
						return e.getVeranstaltung().getKategorie();
					} else {
						return "";
					}
				case 2:
					if (e.getFunktion() == null) {
						return "";
					} else {
						return e.getFunktion();
					}
				case 3:
					if (e.getGage() == null) {
						return "";
					} else {
						return e.getGage().toString();
					}
				}
				return null;
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
				// nothing to do
			}

			@Override
			public void dispose() {
				// nothing to do
			}

			@Override
			public boolean isLabelProperty(Object arg0, String arg1) {
				return true;
			}

			@Override
			public void removeListener(ILabelProviderListener arg0) {
				// nothing to do
			}
		});

		TableColumn colVeranstaltung = new TableColumn(this.tableViewer
				.getTable(), SWT.LEFT);
		colVeranstaltung.setText("Veranstaltung");
		TableColumn colKategorie = new TableColumn(this.tableViewer.getTable(),
				SWT.LEFT);
		colKategorie.setText("Kategorie");
		TableColumn colFunktion = new TableColumn(this.tableViewer.getTable(),
				SWT.LEFT);
		colFunktion.setText("Funktion");
		TableColumn colGage = new TableColumn(this.tableViewer.getTable(),
				SWT.LEFT);
		colGage.setText("Gage");

		this.toolkit.adapt(this.tableViewer.getTable(), true, true);
		engagementSection.setClient(this.tableViewer.getTable());
	}
	
	private void createSaveButton(Composite parent){
		this.btnSave = new Button(parent, SWT.PUSH);
		this.btnSave.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false));
		this.btnSave.setText("Speichern ");
		this.btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!KuenstlerPart.this.dirty.isDirty()) {
					return;
				}
				handlerService.activateHandler("at.ticketline.handler.savePartHandler", new SavePartHandler());
				
				ParameterizedCommand cmd = commandService.createCommand("at.ticketline.handler.savePartHandler", null);
				try{
					handlerService.executeHandler(cmd);
				} catch (Exception ex) {
					LOG.error(ex.getMessage(), ex);
					MessageDialog.openError(KuenstlerPart.this.shell, "Error",
							"Kuenstler kann nicht gespeichert werden: "
									+ ex.getMessage());
				}
			}
		});

	}
	
	private void setInput(){
		this.form.setText(this.kuenstler.getName());
		if ((this.kuenstler.getEngagements() != null)
				&& (this.kuenstler.getEngagements().size() > 0)) {
			this.tableViewer.setInput(this.kuenstler.getEngagements());
		}

		if (this.kuenstler.getGeschlecht() == Geschlecht.WEIBLICH) {
			this.cbGeschlecht.select(1);
		} else {
			this.cbGeschlecht.select(0);
		}
		if (this.kuenstler.getGeburtsdatum() != null) {
			GregorianCalendar gc = this.kuenstler.getGeburtsdatum();
			this.dtGeburtsdatum.setYear(gc.get(Calendar.YEAR));
			this.dtGeburtsdatum.setMonth(gc.get(Calendar.MONTH));
			this.dtGeburtsdatum.setDay(gc.get(Calendar.DAY_OF_MONTH));
		}
	}

	@PreDestroy
	public void dispose(){
		// nothing to do
	}
	
	@Focus
	public void setFocus() {
		// nothing to do
	}
	
	@Persist
	public void save() {
		LOG.info("Künstler speichern");
		if(this.txtNachname.getText().equals("")){
			this.kuenstler.setNachname(null);
		}
		else{
			this.kuenstler.setNachname(this.txtNachname.getText());
		}
		if(this.txtVorname.getText().equals("")){
			this.kuenstler.setVorname(null);
		}
		else{
			this.kuenstler.setVorname(this.txtVorname.getText());
		}
		this.kuenstler.setTitel(this.txtTitel.getText());
		this.kuenstler.setGeschlecht(Geschlecht.getValueOf(this.cbGeschlecht
				.getText()));
		if (this.kuenstler.getGeburtsdatum() == null) {
			this.kuenstler.setGeburtsdatum(new GregorianCalendar());
		}
		this.kuenstler.getGeburtsdatum().set(Calendar.YEAR,
				this.dtGeburtsdatum.getYear());
		this.kuenstler.getGeburtsdatum().set(Calendar.MONTH,
				this.dtGeburtsdatum.getMonth());
		this.kuenstler.getGeburtsdatum().set(Calendar.DAY_OF_MONTH,
				this.dtGeburtsdatum.getDay());
		this.kuenstler.setBiographie(this.txtBiographie.getText());

		try {
			this.kuenstlerService.save(kuenstler);
			this.dirty.setDirty(false);
			
			MessageDialog.openInformation(this.shell, "Speichervorgang", "Künstler wurde erfolgreich gespeichert");
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
			MessageDialog.openError(this.shell, "Error",
					"Künstler konnte nicht gespeichert werden: " + e.getMessage());
		}
	}
	
	private void updateTitle() {
		partService.getActivePart().setLabel(this.txtVorname.getText() + " "
				+ this.txtNachname.getText());
	}

	class EditorModifyListener implements ModifyListener, FocusListener {

		@Override
		public void modifyText(ModifyEvent e) {
			if ((e.getSource().equals(KuenstlerPart.this.txtNachname))
					|| (e.getSource().equals(KuenstlerPart.this.txtVorname))) {
				KuenstlerPart.this.updateTitle();
			}
			dirty.setDirty(true);
		}

		@Override
		public void focusGained(FocusEvent e) {
			// nothing to do
		}

		@Override
		public void focusLost(FocusEvent e) {
			if (e.getSource().equals(KuenstlerPart.this.dtGeburtsdatum) == false) {
				return;
			}
			try {
				GregorianCalendar gc = KuenstlerPart.this.kuenstler
						.getGeburtsdatum();
				if (gc == null) {
					dirty.setDirty(true);
					return;
				}
				if ((KuenstlerPart.this.dtGeburtsdatum.getYear() != gc
						.get(Calendar.YEAR))
						|| (KuenstlerPart.this.dtGeburtsdatum.getMonth() != gc
								.get(Calendar.MONTH))
						|| (KuenstlerPart.this.dtGeburtsdatum.getDay() != gc
								.get(Calendar.DAY_OF_MONTH))) {
					dirty.setDirty(true);
				}
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}

		}
	}
}
