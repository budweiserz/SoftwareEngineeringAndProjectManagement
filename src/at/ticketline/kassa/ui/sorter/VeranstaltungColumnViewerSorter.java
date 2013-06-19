package at.ticketline.kassa.ui.sorter;

import org.eclipse.jface.viewers.Viewer;

import at.ticketline.entity.Veranstaltung;

public class VeranstaltungColumnViewerSorter extends AbstractColumnViewerSorter {
	public static final int BEZEICHNUNG = 0;
	public static final int DAUER = 1;
	public static final int KATEGORIE = 2;
	public static final int INHALT = 3;

	@Override
    public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Veranstaltung v1 = (Veranstaltung)e1;
		Veranstaltung v2 = (Veranstaltung)e2;
		
		switch (this.column) {
			case 0 : rc = v1.getBezeichnung().compareTo(v2.getBezeichnung());
				break;
			case 1 : rc = v1.getDauer().compareTo(v2.getDauer());
				break;
			case 2 : rc = v1.getKategorie().compareTo(v2.getKategorie());
				break;
			case 3 : rc = v1.getInhalt().compareTo(v2.getInhalt());
				break;
		}

		if (this.direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
