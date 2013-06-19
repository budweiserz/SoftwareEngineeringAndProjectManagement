package at.ticketline.kassa.ui.sorter;

import org.eclipse.jface.viewers.Viewer;

import at.ticketline.entity.Kunde;

public class KundeColumnViewerSorter extends AbstractColumnViewerSorter {
	public static final int VORNAME = 0;
	public static final int NACHNAME = 1;
	public static final int GEBURTSDATUM = 2;
	public static final int PUNKTE = 3;

	@Override
    public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Kunde k1 = (Kunde)e1;
		Kunde k2 = (Kunde)e2;
		
		switch (this.column) {
			case 0 : rc = k1.getVorname().compareTo(k2.getVorname());
				break;
			case 1 : rc = k1.getNachname().compareTo(k2.getNachname());
				break;
			case 2 : rc = k1.getGeburtsdatum().compareTo(k2.getGeburtsdatum());
				break;
			case 3 : rc = k1.getPunkte().compareTo(k2.getPunkte());
				break;
		}

		if (this.direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
