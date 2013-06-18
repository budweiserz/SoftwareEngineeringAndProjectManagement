package at.ticketline.kassa.ui;

import org.eclipse.jface.viewers.Viewer;

import at.ticketline.entity.Auffuehrung;

public class AuffuehrungColumnViewerSorter extends AbstractColumnViewerSorter {
	public static final int VERANSTALTUNG = 0;
	public static final int DATUM = 1;
	public static final int ORT = 2;
	public static final int SAAL = 3;

	public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Auffuehrung a1 = (Auffuehrung)e1;
		Auffuehrung a2 = (Auffuehrung)e2;
		
		switch (this.column) {
			case 0 : rc = a1.getVeranstaltung().getBezeichnung().compareTo(a2.getVeranstaltung().getBezeichnung());
				break;
			case 1 : rc = a1.getDatumuhrzeit().compareTo(a2.getDatumuhrzeit());
				break;
			case 2 : rc = a1.getSaal().getOrt().getBezeichnung().compareTo(a2.getSaal().getOrt().getBezeichnung());
				break;
			case 3 : rc = a1.getSaal().getBezeichnung().compareTo(a2.getSaal().getBezeichnung());
				break;
		}

		if (this.direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
