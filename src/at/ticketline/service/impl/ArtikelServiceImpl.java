package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.List;

import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.entity.Artikel;
import at.ticketline.service.api.ArtikelService;

public class ArtikelServiceImpl implements ArtikelService {

	private ArtikelDao artikelDao;
	
	public ArtikelServiceImpl(ArtikelDao artikelDao) {
		this.artikelDao = artikelDao;
	}

	@Override
	public List<Artikel> findAll() {
		return (artikelDao.findAll() != null) ? artikelDao.findAll() : new ArrayList<Artikel>();
	}

}
