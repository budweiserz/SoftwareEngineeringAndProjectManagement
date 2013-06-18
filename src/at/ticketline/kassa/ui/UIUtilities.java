package at.ticketline.kassa.ui;

import java.text.Collator;
import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class UIUtilities {

	/**
	 * Generates german constraint-violation message for user output.
	 * 
	 * @param c
	 *            != null
	 * @return constraint violations, separated by "\n", occurences of
	 *         "may not be null" replaced by "muss ausgefüllt werden"
	 */
	public static String getReadableConstraintViolations(ConstraintViolationException c) {

		StringBuilder sb = new StringBuilder("Die eingegebenen Daten weisen folgende Fehler auf:\n");

		for (ConstraintViolation<?> cv : c.getConstraintViolations()) {
			sb.append(cv.getPropertyPath().toString().toUpperCase()).append(" ").append(cv.getMessage() + "\n");
		}

		return sb.toString().replaceAll("may not be null", "muss ausgefüllt sein");
	}

	public static void addTableViewerColumnSorter(final TableViewer table, final TableViewerColumn column, final int columnName) {
		final TableColumn tc = column.getColumn();

		tc.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				((AbstractColumnViewerSorter) table.getSorter()).doSort(table, tc, columnName);
				table.refresh();
			}
		});
	}
}

