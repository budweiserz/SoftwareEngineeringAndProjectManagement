package at.ticketline.service.api;

import at.ticketline.entity.Mitarbeiter;

public interface MitarbeiterService {
    
	/**
	 * Speichert einen neuen bzw. einen bereits bestehenden Mitarbeiter.
	 *  
	 * @param mitarbeiter Der Mitarbeiter, der gespeichert werden soll
	 */
	public void save(Mitarbeiter mitarbeiter);
	
	/**
	 * Einloggen mit den übergebenen Benutzernamen und dem dazugehörigen 
	 * Passworts.
	 * 
	 * @param username Benutzername, mit dem man angemeldet werden möchte
	 * @param password Passwort des Benutzers
	 * @return true - falls Login erfolgreich war, ansonsten false
	 */
	public boolean login(String username, String password);
}
