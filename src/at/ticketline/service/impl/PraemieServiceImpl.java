package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.List;

import at.ticketline.dao.api.PraemieDao;
import at.ticketline.entity.Praemie;
import at.ticketline.service.api.PraemieService;

public class PraemieServiceImpl implements PraemieService {

	private PraemieDao praemieDao;
	
	public PraemieServiceImpl(PraemieDao praemieDao) {
		this.praemieDao = praemieDao;
	}

	@Override
	public List<Praemie> findAll() {
		return (praemieDao.findAll() != null) ? praemieDao.findAll() : new ArrayList<Praemie>();
	}
}