package at.ticketline.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.AuffuehrungService;

/**
 * @author Michael Ion
 */
public class AuffuehrungServiceImpl implements AuffuehrungService {

    private static final Logger LOG = LoggerFactory.getLogger(AuffuehrungServiceImpl.class);
    private AuffuehrungDao auffuehrungDao;
    
    public AuffuehrungServiceImpl(AuffuehrungDao auffuehrungDao) {
        this.auffuehrungDao = auffuehrungDao;
    }
    
    @Override
    public List<Auffuehrung> find(Auffuehrung auffuehrung, Auffuehrung to) {
        LOG.info("Suche nach Auffuehrung");
        return auffuehrungDao.findByAuffuehrung(auffuehrung, to);
    }

    @Override
    public void save(Auffuehrung auffuehrung) {
        try {
            if(auffuehrung == null) {
                LOG.info("Persistieren gescheitert: Auffuehrung ist null.");
                throw new ServiceException("Argument darf nicht null sein");
            }
            if(auffuehrung.getId() == null) {
                LOG.info("Speichere neue Auffuehrung ab...");
                this.auffuehrungDao.persist(auffuehrung);
                
            } else {
                LOG.info("Speichere vorhandene Auffuehrung ab...");
                this.auffuehrungDao.merge(auffuehrung);
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

}
