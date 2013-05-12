package at.ticketline.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.KuenstlerService;

public class KuenstlerServiceImpl implements KuenstlerService {
    private static final Logger LOG = LoggerFactory.getLogger(KuenstlerServiceImpl.class);
    
    private KuenstlerDao kuenstlerDao;

    public KuenstlerServiceImpl(KuenstlerDao kuenstlerDao) {
        this.kuenstlerDao = kuenstlerDao;
    }

    @Override
    public void save(Kuenstler kuenstler) {
        try {
            if (kuenstler.getId() == null) {
                LOG.info("Persisting new artist {} {}", kuenstler.getVorname(), kuenstler.getNachname());
                this.kuenstlerDao.persist(kuenstler);
            } else {
                LOG.info("Persisting artist {} ({} {})", kuenstler.getId(), kuenstler.getVorname(), kuenstler.getNachname());
                this.kuenstlerDao.merge(kuenstler);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        
    }

    public List<Kuenstler> findByKuenstler(Kuenstler query) {
    	
    	List<Kuenstler> found = null;
    	try {
    		found = this.kuenstlerDao.findByKuenstler(query);
    		LOG.debug("Suche nach Kuenstler: {} {} {}", query.getVorname(), query.getNachname(), query.getGeschlecht());
    		for (Kuenstler k: found) {
    			LOG.debug("Kuenstler gefunden: {} {} {}", k.getVorname(), k.getNachname(), k.getGeschlecht());
    		}
    	} catch (DaoException e) {
    		throw new ServiceException(e);
    	}
    	
    	return found;
    }
    
    public List<Kuenstler> findByKuenstlerWithAnySex(Kuenstler query) {
    	
    	if (query == null) {
    		throw new ServiceException("Der Suchparameter 'kuenstler' darf nicht null sein");
    	}
    	
    	LOG.debug("Suche nach Kuenstler mit irgendeinem Geschlecht");
    	query.setGeschlecht(Geschlecht.MAENNLICH);
    	List<Kuenstler> found = this.findByKuenstler(query);
    	query.setGeschlecht(Geschlecht.WEIBLICH);
    	found.addAll(this.findByKuenstler(query));
    	
    	return found;    	
    }
    
}
