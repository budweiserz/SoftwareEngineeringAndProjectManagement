package at.ticketline.dao.api;

import java.util.Date;
import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Veranstaltung;

public interface VeranstaltungDao extends GenericDao<Veranstaltung,Integer> {
	public List<Veranstaltung> findByVeranstaltung(Veranstaltung query, Integer minDauer, Integer maxDauer);
}
