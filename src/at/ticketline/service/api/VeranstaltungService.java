package at.ticketline.service.api;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import at.ticketline.entity.Veranstaltung;

public interface VeranstaltungService {
	/**
	 * Sucht nach ähnlichen Veranstaltungen, die mit der übergebenen Veranstaltung übereinstimmen
	 * 
	 * @param veranstaltung Das Veranstaltungsobjekt, dass die Suchkriterien beinhaltet. Die Dauer Variable
	 * wird beim Suchen ignoriert.
	 * @param minDauer die minimale Dauer, die die gefundenen Veranstaltungen haben sollen
	 * @param maxDauer die maximale Dauer, die die gefundenen Veranstaltungen haben sollen
	 * @return Eine Liste aller gefundenen Veranstaltungen
	 */
	public List<Veranstaltung> find(Veranstaltung veranstaltung, Integer minDauer, Integer maxDauer);
	
	/**
	 * Speichert eine neue bzw. eine bereits bestehende Veranstaltung.
	 *  
	 * @param veranstaltung Die Veranstaltung, die gespeichert werden soll
	 */
	public void save(Veranstaltung veranstaltung);
	
	/**
	 * Liefert die 10 Veranstaltungen mit den meisten verkauften Tickets.
	 * 
	 * @param start Startdatum, von dem Angefangen werden soll zu suchen
	 * @param end Enddatum, bis dahin wird nach den Top 10 Veranstaltungen gesucht
	 * @param kategorie Die Kategorie der Veranstaltung
	 * @return Eine Liste der Top 10 Veranstaltungen und deren Anzahl an verkauften Tickets
	 */
	public LinkedHashMap<Veranstaltung, Integer> findTopTen(Date start, Date end, String kategorie);
}
