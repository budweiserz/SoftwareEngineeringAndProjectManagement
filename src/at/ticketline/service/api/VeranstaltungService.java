package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Veranstaltung;

public interface VeranstaltungService {
	/**
	 * Sucht nach ähnlichen Veranstaltungen, die mit der übergebenen Veranstaltung übereinstimmen
	 * 
	 * @param veranstaltung Das Veranstaltungsobjekt, dass die Suchkriterien beinhaltet
	 * @return Eine Liste aller gefundenen Veranstaltungen
	 */
	public List<Veranstaltung> find(Veranstaltung veranstaltung);
	
	/**
	 * Speichert eine neue bzw. eine bereits bestehende Veranstaltung.
	 *  
	 * @param veranstaltung Die Veranstaltung, die gespeichert werden soll
	 */
	public void save(Veranstaltung veranstaltung);
}
