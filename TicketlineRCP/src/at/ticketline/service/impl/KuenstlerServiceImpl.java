package at.ticketline.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.KuenstlerDao;
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

}
