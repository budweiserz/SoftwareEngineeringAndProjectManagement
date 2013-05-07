package at.ticketline.dao.api;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Mitarbeiter;

public interface MitarbeiterDao extends GenericDao<Mitarbeiter,Integer> {
	public Mitarbeiter findByUsername(String username);
}
