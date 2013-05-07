package at.ticketline.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.MitarbeiterService;

public class MitarbeiterServiceImpl implements MitarbeiterService {
	private static final Logger LOG = LoggerFactory.getLogger(MitarbeiterServiceImpl.class);
	
	private MitarbeiterDao mitarbeiterDao;

	public MitarbeiterServiceImpl(MitarbeiterDao mitarbeiterDao) {
		this.mitarbeiterDao = mitarbeiterDao;
	}
	
	/**
	 * Speichert einen neuen bzw. einen bereits bestehenden Mitarbeiter.
	 *  
	 * @param mitarbeiter Der Mitarbeiter, der gespeichert werden soll
	 */
	@Override
	public void save(Mitarbeiter mitarbeiter) {
		try {
			if (mitarbeiter.getId() == null) {
				LOG.info("Neuen Mitarbeiter speichern: {} ({} {})", mitarbeiter.getUsername(), mitarbeiter.getVorname(), mitarbeiter.getNachname());
				this.mitarbeiterDao.persist(mitarbeiter);
			} else {
				LOG.info("Bestehenden Mitarbeiter speichern: {} ({}, {} {})", mitarbeiter.getUsername(), mitarbeiter.getId(), mitarbeiter.getVorname(), mitarbeiter.getNachname());
				this.mitarbeiterDao.merge(mitarbeiter);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	/**
	 * Einloggen mit den übergebenen Benutzernamen und des dazugehörigen Passworts.
	 * 
	 * @param username Benutzername, mit dem man angemeldet werden möchte
	 * @param password Passwort des Benutzers
	 * @return true - falls Login erfolgreich war, ansonsten false
	 */
	@Override
	public boolean login(String username, String password) {
		Mitarbeiter m;
		
		// Benutzername und Passwort wurde eingegeben
		if (!username.trim().equals("") && !password.trim().equals("")) {
			m = this.mitarbeiterDao.findByUsername(username);
			
			// falls Passwort mit dem des Benutzernamens übereinstimmt, dann ist Login erfolgreich
			if (m.getPasswort().equals(password)) {
				LOG.info("Erfolgreicher Login mit Benutzername: {}, Passwort: {}", username, password);
				return true;
			}
		}
		
		LOG.info("Misslungener Login mit Benutzername: {}, Passwort: {}", username, password);
		return false;
	}
}
