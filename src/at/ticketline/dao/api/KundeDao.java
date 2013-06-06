package at.ticketline.dao.api;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Kunde;

public interface KundeDao extends GenericDao<Kunde,Integer> {

    List<Kunde> findByKunde(Kunde query);

}
