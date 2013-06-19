package at.ticketline.kassa.ui.sorter;

import org.eclipse.jface.viewers.Viewer;

import at.ticketline.entity.Transaktion;

public class TicketColumnViewerSorter extends AbstractColumnViewerSorter {
	public static final int TYP = 0;
	public static final int NUMMER = 1;
	public static final int VORNAME = 2;
	public static final int NACHNAME = 3;
	public static final int VERANSTALTUNG = 4;

	@Override
    public int compare(Viewer viewer, Object e1, Object e2) {
		int rc = 0;
		Transaktion t1 = (Transaktion)e1;
		Transaktion t2 = (Transaktion)e2;
		
		String bez1;
		String bez2;
		String vorname1;
		String vorname2;
		String nachname1;
		String nachname2;
		
		if (t1.getKunde() == null) {
			vorname1 = "Anonym";
			nachname1 = "-";
		}
		else {
			vorname1 = t1.getKunde().getVorname();
			nachname1 = t1.getKunde().getNachname();
		}
		
		if (t2.getKunde() == null) {
			vorname2 = "Anonym";
			nachname2 = "-";
		}
		else {
			vorname2 = t2.getKunde().getVorname();
			nachname2 = t2.getKunde().getNachname();
		}
		
		switch (this.column) {
			case 0 : rc = t1.getStatus().compareTo(t2.getStatus());
				break;
			case 1 : rc = t1.getReservierungsnr().compareTo(t2.getReservierungsnr());
				break;
			case 2 : rc = vorname1.compareTo(vorname2);
				break;
			case 3 : rc = nachname1.compareTo(nachname2);
				break;
			case 4 : 
					if (t1.getPlaetze() != null && t1.getPlaetze().iterator().next() != null 
			                && t1.getPlaetze().iterator().next().getAuffuehrung() != null 
			                && t1.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung() != null) {
			            bez1 = t1.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung().getBezeichnung();
			        } else {
			            bez1 = "";
			        }
					
					if (t2.getPlaetze() != null && t2.getPlaetze().iterator().next() != null 
			                && t2.getPlaetze().iterator().next().getAuffuehrung() != null 
			                && t2.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung() != null) {
			            bez2 = t2.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung().getBezeichnung();
			        } else {
			            bez2 = "";
			        }
					
					rc = bez1.compareTo(bez2);
				break;
		}

		if (this.direction == DESCENDING)
			rc = -rc;
		return rc;
	}
}
