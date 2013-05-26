package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import at.ticketline.dao.api.BestellungDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;
import at.ticketline.service.api.BestellungService;

public class BestellungServiceImpl implements BestellungService {
	
	private BestellungDao bestellungDao;

	public BestellungServiceImpl(BestellungDao bestellungDao) {
		this.bestellungDao = bestellungDao;
	}

	@Override
	public void saveBestellungen(HashMap<Artikel, Integer> bestellungen) {
		
	}

	@Override
	public List<Bestellung> findAll() {
		return (bestellungDao.findAll() != null) ? bestellungDao.findAll() : new ArrayList<Bestellung>();
	}

}
