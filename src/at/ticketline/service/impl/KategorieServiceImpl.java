package at.ticketline.service.impl;

import java.util.List;

import at.ticketline.dao.api.KategorieDao;
import at.ticketline.entity.Kategorie;
import at.ticketline.service.api.KategorieService;

public class KategorieServiceImpl implements KategorieService {
	
	private KategorieDao kategorieDao;
	
	public KategorieServiceImpl(KategorieDao kategorieDao) {
		this.kategorieDao = kategorieDao;		
	}

	@Override
	public List<Kategorie> findAll() {
		return this.kategorieDao.findAll();
	}

}
