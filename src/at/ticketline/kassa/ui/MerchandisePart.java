package at.ticketline.kassa.ui;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.service.impl.ArtikelServiceImpl;

public class MerchandisePart {

	private FormToolkit toolkit;
	private TableViewer tableViewer;
	private ArtikelServiceImpl artikelService;

	public MerchandisePart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		this.toolkit = new FormToolkit(parent.getDisplay());
		this.createTable(parent);
	}
	
	private void createTable(Composite parent) {
    
        this.tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        TableLayout layout = new TableLayout();
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        layout.addColumnData(new ColumnWeightData(33, 100, true));
        //layout.addColumnData(new ColumnWeightData(15, 100, true));
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
                Artikel e = (Artikel) element;
                switch (index) {
                case 0:
                    if (e.getKurzbezeichnung() != null) {
                        return e.getKurzbezeichnung();
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
        });
    
        TableColumn col1 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        col1.setText("Merchandiseartikel");
        //TableColumn col4 = new TableColumn(this.tableViewer.getTable(), SWT.LEFT);
        //col4.setText("Gage");
        
        // MAGIC HAPPENS HERE
        this.artikelService = new ArtikelServiceImpl((ArtikelDao)DaoFactory.getByEntity(Artikel.class));
        
        this.tableViewer.setInput(this.artikelService.findAll());

    
        this.toolkit.adapt(this.tableViewer.getTable(), true, true);
    }

	@PreDestroy
	public void dispose() {
	}

}
