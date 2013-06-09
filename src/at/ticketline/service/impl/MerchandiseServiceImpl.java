package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.List;

import at.ticketline.dao.api.MerchandiseDao;
import at.ticketline.entity.Merchandise;
import at.ticketline.service.api.MerchandiseService;

public class MerchandiseServiceImpl implements MerchandiseService {

	private MerchandiseDao MerchandiseDao;
	
	public MerchandiseServiceImpl(MerchandiseDao MerchandiseDao) {
		this.MerchandiseDao = MerchandiseDao;
	}

	@Override
	public List<Merchandise> findAll() {
		return (MerchandiseDao.findAll() != null) ? MerchandiseDao.findAll() : new ArrayList<Merchandise>();
	}

}
