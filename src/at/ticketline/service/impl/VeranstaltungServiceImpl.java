package at.ticketline.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.VeranstaltungService;

public class VeranstaltungServiceImpl implements VeranstaltungService {
	private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungServiceImpl.class);
	
	private VeranstaltungDao veranstaltungDao;

	public VeranstaltungServiceImpl(VeranstaltungDao veranstaltungDao) {
		this.veranstaltungDao = veranstaltungDao;
	}
	
	/**
	 * Sucht nach ähnlichen Veranstaltungen, die mit der übergebenen Veranstaltung übereinstimmen
	 * 
	 * @param veranstaltung Das Veranstaltungsobjekt, dass die Suchkriterien beinhaltet
	 * @return Eine Liste aller gefundenen Veranstaltungen
	 */
	@Override
	public List<Veranstaltung> find(Veranstaltung veranstaltung) {
		return this.veranstaltungDao.findByVeranstaltung(veranstaltung);
	}

	/**
	 * Speichert eine neue bzw. eine bereits bestehende Veranstaltung.
	 *  
	 * @param veranstaltung Die Veranstaltung, die gespeichert werden soll
	 */
	@Override
	public void save(Veranstaltung veranstaltung) {
		try {
			if (veranstaltung.getId() == null) {
				LOG.info("Neue Veranstaltung speichern: {} (Kategorie: {}, Inhalt: {})", veranstaltung.getBezeichnung(), veranstaltung.getKategorie(), veranstaltung.getInhalt());
				this.veranstaltungDao.persist(veranstaltung);
			} else {
				LOG.info("Bestehende Veranstaltung speichern: {} (ID: {})", veranstaltung.getBezeichnung(), veranstaltung.getId());
				this.veranstaltungDao.merge(veranstaltung);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
}
