package at.ticketline.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Ort;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.OrtService;

public class OrtServiceImpl implements OrtService {
	private static final Logger LOG = LoggerFactory.getLogger(OrtServiceImpl.class);
	
	private OrtDao ortDao;
	
	public OrtServiceImpl(OrtDao ortDao) {
		this.ortDao = ortDao;
	}

	@Override
	public List<Ort> findByOrt(Ort query) {
		
		List<Ort> orte = null;
		
		try {
			orte = ortDao.findByOrt(query);
		} catch (DaoException e) {
			LOG.info("Suche nach Ort {} schlug fehl", query);
			throw new ServiceException(e);
		}
		
		for(Ort o : orte) {
		    LOG.debug("Ort gefunden: {}", o.toString());
		}
		
		return orte;
	}

}
