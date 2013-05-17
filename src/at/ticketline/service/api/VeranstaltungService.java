package at.ticketline.service.api;

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
}
