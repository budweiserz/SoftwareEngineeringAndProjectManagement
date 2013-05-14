package at.ticketline.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.entity.Kunde;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.KundeService;

/**
 * @author Rafael Konlechner
 */
public class KundeServiceImpl implements KundeService {
    private static final Logger LOG = LoggerFactory.getLogger(KundeServiceImpl.class);

    private KundeDao kundeDao;

    public KundeServiceImpl(KundeDao kundeDao) {
        this.kundeDao = kundeDao;
    }

    /**
     * Speichert einen neuen bzw. bestehenden Kunden, wenn die Attributwerte
     * gültig sind (laut Validator).
     * 
     * @param kunde
     *            != null, wird gespeichert
     */
    @Override
    public void save(Kunde kunde) {
        try {
            
            if (kunde == null) {
                LOG.info("Persisting failed: Argument null");
                throw new ServiceException("Argument darf nicht null sein");
            }
            if (kunde.getId() == null) {
                LOG.info("Persisting new customer {} {}", kunde.getVorname(), kunde.getNachname());
                this.kundeDao.persist(kunde);
            } else {
                LOG.info("Persisting customer {} ({} {})", kunde.getId(), kunde.getVorname(), kunde.getNachname());
                this.kundeDao.merge(kunde);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }
    
    /**
     * @return alle Kunden
     */
    @Override
    public List<Kunde> findAll() {
    	
    	List<Kunde> found = null;
    	try {
    		found = this.kundeDao.findAll();
    		LOG.info("List all Kunden");
    		for (Kunde k: found) {
    			LOG.info("Kunde gefunden: {} {} {}", k.getVorname(), k.getNachname(), k.getGeschlecht());
    		}
    	} catch (DaoException e) {
    		throw new ServiceException(e);
    	}
    	
    	return found;
    }
}