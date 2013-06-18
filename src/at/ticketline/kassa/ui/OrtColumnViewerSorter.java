package at.ticketline.kassa.ui;

import org.eclipse.jface.viewers.Viewer;

import at.ticketline.entity.Ort;

public class OrtColumnViewerSorter extends AbstractColumnViewerSorter {
	public static final int BEZEICHNUNG = 0;
	public static final int STRASSE = 1;
	public static final int PLZ = 2;
	public static final int ORT = 3;
	public static final int ORTSTYP = 4;
	public static final int LAND = 5;

	public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Ort o1 = (Ort)e1;
		Ort o2 = (Ort)e2;
		
		switch (this.column) {
			case 0 : rc = o1.getBezeichnung().compareTo(o2.getBezeichnung());
				break;
			case 1 : rc = o1.getAdresse().getStrasse().compareTo(o2.getAdresse().getStrasse());
				break;
			case 2 : rc = o1.getAdresse().getPlz().compareTo(o2.getAdresse().getPlz());
				break;
			case 3 : rc = o1.getAdresse().getOrt().compareTo(o2.getAdresse().getOrt());
				break;
			case 4 : rc = o1.getOrtstyp().compareTo(o2.getOrtstyp());
				break;
			case 5 : rc = o1.getAdresse().getLand().compareTo(o2.getAdresse().getLand());
				break;
		}

		if (this.direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
