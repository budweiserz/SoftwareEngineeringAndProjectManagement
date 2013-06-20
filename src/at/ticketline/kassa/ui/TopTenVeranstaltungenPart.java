package at.ticketline.kassa.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.jfree.util.Rotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.VeranstaltungService;

@SuppressWarnings("restriction")
public class TopTenVeranstaltungenPart {
	private static final Logger LOG = LoggerFactory.getLogger(TopTenVeranstaltungenPart.class);
    
    @Inject private MDirtyable dirty;
    @Inject private EPartService partService;
    @Inject private EHandlerService handlerService;
    @Inject private ECommandService commandService;
    @Inject private ESelectionService selectionService;
    @Inject private MPart activePart;
    //@Inject @Named (IServiceConstants.ACTIVE_SHELL) private Shell shell;
    
    @Inject private VeranstaltungService veranstaltungService;
    
    private TableViewer tableViewer;
	
    private Button btnAnzeigen;
	//private Table table;
	private DateTime dateTimeVon;
	private DateTime dateTimeBis;
	private Combo combo_1;
	private Table table_topTenVeranstaltungen;
	private Composite topTenVeranstaltungenComposite;
	private ChartComposite chartC;
	
	private int veranstaltungenTablePositionIndex;
	
	public TopTenVeranstaltungenPart() {
		this.veranstaltungenTablePositionIndex = 0;
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Composite SearchComposite = new Composite(parent, SWT.BORDER);
		SearchComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		SearchComposite.setLayout(new FormLayout());
		GridData gd_SearchComposite = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_SearchComposite.heightHint = 100;
		gd_SearchComposite.widthHint = 1920;
		gd_SearchComposite.minimumHeight = 100;
		gd_SearchComposite.minimumWidth = 600;
		SearchComposite.setLayoutData(gd_SearchComposite);
		
		Label lblVon = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblVon = new FormData();
		fd_lblVon.top = new FormAttachment(0, 10);
		fd_lblVon.left = new FormAttachment(0, 10);
		lblVon.setLayoutData(fd_lblVon);
		lblVon.setText("Von");
		
		dateTimeVon = new DateTime(SearchComposite, SWT.BORDER);
		dateTimeVon.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_dateTimeVon = new FormData();
		fd_dateTimeVon.top = new FormAttachment(lblVon, -5, SWT.TOP);
		fd_dateTimeVon.right = new FormAttachment(lblVon, 135, SWT.RIGHT);
		fd_dateTimeVon.left = new FormAttachment(lblVon, 10);
		dateTimeVon.setLayoutData(fd_dateTimeVon);
		
		Label lblBis = new Label(SearchComposite, SWT.NONE);
		FormData fd_lblBis = new FormData();
		fd_lblBis.top = new FormAttachment(0, 10);
		fd_lblBis.left = new FormAttachment(dateTimeVon, 30);
		lblBis.setLayoutData(fd_lblBis);
		lblBis.setText("Bis");
		
		dateTimeBis = new DateTime(SearchComposite, SWT.BORDER);
		dateTimeBis.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		FormData fd_dateTimeBis = new FormData();
		fd_dateTimeBis.top = new FormAttachment(lblBis, -5, SWT.TOP);
		fd_dateTimeBis.right = new FormAttachment(lblBis, 135, SWT.RIGHT);
		fd_dateTimeBis.left = new FormAttachment(lblBis, 10);
		dateTimeBis.setLayoutData(fd_dateTimeBis);
		
		Label lblKategorie = new Label(SearchComposite, SWT.NONE);
		lblKategorie.setText("Kategorie");
		FormData fd_lblKategorie = new FormData();
		fd_lblKategorie.bottom = new FormAttachment(lblVon, 0, SWT.BOTTOM);
		fd_lblKategorie.left = new FormAttachment(dateTimeBis, 32);
		lblKategorie.setLayoutData(fd_lblKategorie);
		
		combo_1 = new Combo(SearchComposite, SWT.NONE);
		combo_1.setItems(new String[] {"Alle", "Kino", "Theater", "Kabarett"});
		FormData fd_combo_1 = new FormData();
		fd_combo_1.top = new FormAttachment(lblVon, -4, SWT.TOP);
		fd_combo_1.right = new FormAttachment(lblKategorie, 102, SWT.RIGHT);
		fd_combo_1.left = new FormAttachment(lblKategorie, 6);
		combo_1.setLayoutData(fd_combo_1);
		combo_1.setText("Alle");
		
		btnAnzeigen = new Button(SearchComposite, SWT.NONE);
		FormData fd_btnAnzeigen = new FormData();
		fd_btnAnzeigen.bottom = new FormAttachment(100, -10);
		fd_btnAnzeigen.left = new FormAttachment(lblVon, 0, SWT.LEFT);
		btnAnzeigen.setLayoutData(fd_btnAnzeigen);
		btnAnzeigen.setText("Anzeigen");
		
		topTenVeranstaltungenComposite = new Composite(parent, SWT.BORDER | SWT.NO_BACKGROUND);
		topTenVeranstaltungenComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		topTenVeranstaltungenComposite.setLayout(new GridLayout(2, false));
		topTenVeranstaltungenComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		this.tableViewer = new TableViewer(topTenVeranstaltungenComposite, SWT.BORDER | SWT.FULL_SELECTION);
		table_topTenVeranstaltungen = tableViewer.getTable();
		table_topTenVeranstaltungen.setLinesVisible(true);
		table_topTenVeranstaltungen.setHeaderVisible(true);
		GridData gridData_tableViewer = new GridData();
		gridData_tableViewer.horizontalAlignment = SWT.FILL;
		gridData_tableViewer.verticalAlignment = SWT.FILL;
		gridData_tableViewer.grabExcessVerticalSpace = true;
		gridData_tableViewer.widthHint = 420;
		table_topTenVeranstaltungen.setLayoutData(gridData_tableViewer);
		
		
		TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnPlatz = tableViewerColumn.getColumn();
		tblclmnPlatz.setWidth(60);
		tblclmnPlatz.setText("Platz");
		
		TableViewerColumn tableViewerColumn_1 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnVeranstaltung = tableViewerColumn_1.getColumn();
		tblclmnVeranstaltung.setWidth(250);
		tblclmnVeranstaltung.setText("Veranstaltung");
		
		TableViewerColumn tableViewerColumn_2 = new TableViewerColumn(tableViewer, SWT.LEFT);
		TableColumn tblclmnAnzahl = tableViewerColumn_2.getColumn();
		tblclmnAnzahl.setWidth(140);
		tblclmnAnzahl.setText("Verkaufte Tickets");
		
		
		/**
		 * This class provides the content for the table
		 */

		class TopTenVeranstaltungenContentProvider implements IStructuredContentProvider {

		/**
		 * Gets the elements for the table
		 * 
		 * @param arg0
		 *            the model
		 * @return Object[]
		 */
		@SuppressWarnings("unchecked")
        @Override
        public Object[] getElements(Object arg0) {
		    // Returns all the players in the specified team
		    return ((HashMap<Veranstaltung, Integer>)arg0).entrySet().toArray();
		}

		  /**
		   * Disposes any resources
		   */
		  @Override
        public void dispose() {
		    // We don't create any resources, so we don't dispose any
		  }

		  @Override
		  public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			  // TODO Auto-generated method stub
			
		  }
		}
		
        class ColoredLabelProvider implements IBaseLabelProvider, ITableLabelProvider {
        	@Override
            public Image getColumnImage(Object arg0, int arg1) {
                return null;
            }
    
           	@SuppressWarnings("unchecked")
			@Override
            public String getColumnText(Object element, int index) {
                Map.Entry<Veranstaltung, Integer> v = (Map.Entry<Veranstaltung, Integer>) element;
                switch (index) {
                case 0:
                		veranstaltungenTablePositionIndex++;
                		
                		if (veranstaltungenTablePositionIndex > tableViewer.getTable().getItemCount()) {
                			veranstaltungenTablePositionIndex = 1;
                		}
                		
                    	return veranstaltungenTablePositionIndex + "";
                case 1:
                	if (v.getKey().getBezeichnung() != null) {
                        return v.getKey().getBezeichnung();
                    } else {
                        return "";
                    }
                case 2:
                	if (v.getValue() != null) {
                        return v.getValue().toString();
                    } else {
                        return "";
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
        }
        
        this.tableViewer.setContentProvider(new TopTenVeranstaltungenContentProvider());	
        this.tableViewer.setLabelProvider(new ColoredLabelProvider());
        
        this.tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
            	IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection(); 
				Object selectedObj = selection.getFirstElement();
				Veranstaltung v = null;
				
				if (selectedObj != null && selectedObj instanceof Map.Entry<?, ?> ) {
					v = ((Map.Entry<Veranstaltung, Integer>)selectedObj).getKey();
				}
				
				if ( v != null ) {
					selectionService.setSelection(v);
					LOG.info("Selection changed: {}", selection.getFirstElement().toString());
				}
            }
        });
        
        this.tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                ParameterizedCommand c = commandService.createCommand("at.ticketline.command.openVeranstaltung", null);
                handlerService.executeHandler(c);
            }
        });
        
        this.btnAnzeigen.addMouseListener(new MouseListener() {
            @Override public void mouseDoubleClick(MouseEvent e) { }
            @Override public void mouseDown(MouseEvent e) { }

            @Override
            public void mouseUp(MouseEvent e) {
            	Calendar c = new GregorianCalendar();
            	
        		c.set(dateTimeVon.getYear(), dateTimeVon.getMonth(), dateTimeVon.getDay());
        		Date start = c.getTime();
        		c.set(dateTimeBis.getYear(), dateTimeBis.getMonth(), dateTimeBis.getDay());
        		Date end = c.getTime();
        		LinkedHashMap<Veranstaltung, Integer> topTen = veranstaltungService.findTopTen(start, end, combo_1.getText().equals("Alle") ? "" : combo_1.getText());
        		
        		tableViewer.setInput(topTen);
        		
        		PieDataset dataset = createDataset(topTen);  
        		JFreeChart chart = createChart(dataset, "Übersicht über die 10 beliebtesten Veranstaltungen");  
        		
        		// redraw chart with new data
              	chartC.setChart(chart);
              	chartC.redraw();
              	chartC.update();
              	
                tableViewer.refresh();
                
                LOG.info("Refresh Top 10 List: Start {}, End {}, Category: {}", start, end, combo_1.getText());
            }
        });

        Calendar c = new GregorianCalendar();
		c.set(2012, 6, 5);
		Date start = c.getTime();
		c.set(2014, 6, 5);
		Date end = c.getTime();
		LinkedHashMap<Veranstaltung, Integer> topTen = this.veranstaltungService.findTopTen(start, end, null);
		
		this.tableViewer.setInput(topTen);
		
		PieDataset dataset = createDataset(topTen);  
		JFreeChart chart = createChart(dataset, "Übersicht über die 10 beliebtesten Veranstaltungen");  
		
		chartC = new ChartComposite(topTenVeranstaltungenComposite, SWT.NONE, chart, true);
		
		
		GridData gridData_chartC = new GridData();
		gridData_chartC.horizontalAlignment = SWT.FILL;
		gridData_chartC.verticalAlignment = SWT.FILL;
		gridData_chartC.grabExcessVerticalSpace = true;
		gridData_chartC.grabExcessHorizontalSpace = true;
		chartC.setLayoutData(gridData_chartC); 
	}
	
	private PieDataset createDataset(LinkedHashMap<Veranstaltung, Integer> topTen) {  
		final DefaultPieDataset result = new DefaultPieDataset();
		Iterator<Map.Entry<Veranstaltung, Integer>> itTopTenVeranstaltung;
		Map.Entry<Veranstaltung, Integer> currentTopTenVeranstaltung = null;
		
		itTopTenVeranstaltung = topTen.entrySet().iterator();
		
		while (itTopTenVeranstaltung.hasNext()) {
			currentTopTenVeranstaltung = itTopTenVeranstaltung.next();
			
			result.setValue(currentTopTenVeranstaltung.getKey().getBezeichnung(), currentTopTenVeranstaltung.getValue());
		} 
		
		return result;  
	}  
		  
	private org.jfree.chart.JFreeChart createChart(final PieDataset dataset, final String title) {  
		final JFreeChart chart = ChartFactory.createPieChart3D(title, dataset, true, true, false);  
		final PiePlot3D plot = (PiePlot3D) chart.getPlot();  
		plot.setStartAngle(290);  
		plot.setDirection(Rotation.CLOCKWISE);  
		plot.setForegroundAlpha(0.5f);  
		plot.setSectionOutlinesVisible(false);
        plot.setNoDataMessage("Keine Daten verfügbar");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        
		return chart;  
	}  

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
}
