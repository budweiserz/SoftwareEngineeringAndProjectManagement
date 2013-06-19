package at.ticketline.kassa.ui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.TableColumn;

public abstract class AbstractColumnViewerSorter extends ViewerSorter {
	protected static final int ASCENDING = 0;
	protected static final int DESCENDING = 1;

	protected int column;

	protected int direction;

	public void doSort(TableViewer tv, TableColumn tc, int column) {
		if (column == this.column) {
			direction = 1 - direction;
		} else {
			this.column = column;
			direction = ASCENDING;
		}
		
		tv.getTable().setSortColumn(tc);
		tv.getTable().setSortDirection(direction);
	}

	@Override
    public abstract int compare(Viewer viewer, Object e1, Object e2);
}
