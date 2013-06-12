package at.ticketline.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import at.ticketline.dao.api.BestellungDao;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.BestellPosition;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.BestellungService;

public class BestellungServiceImpl implements BestellungService {
	
	private BestellungDao bestellungDao;

	public BestellungServiceImpl(BestellungDao bestellungDao) {
		this.bestellungDao = bestellungDao;
	}

	@Override
	public void saveBestellungen(HashMap<Artikel, Integer> bestellungen, Zahlungsart art, Kunde kunde) {
		
		if (bestellungen == null) {
			throw new IllegalArgumentException("Argument bestellungen should not be null!");
		}
		
		Bestellung b = new Bestellung();
		b.setKunde(kunde);
		b.setZahlungsart(art);
		b.setBestellzeitpunkt(new Date());
		b.setBestellPositionen(new HashSet<BestellPosition>());
		for(Map.Entry<Artikel, Integer> e: bestellungen.entrySet()) {
			if(e.getValue() != 0) {
				BestellPosition p = new BestellPosition();
				p.setArtikel(e.getKey());
				p.setMenge(e.getValue());
				p.setBestellung(b);
				b.getBestellPositionen().add(p);
			}
		}
		
		if (b.getBestellPositionen().size() > 0) {
			this.bestellungDao.persist(b);
		}
	}

	@Override
	public List<Bestellung> findAll() {
		return (bestellungDao.findAll() != null) ? bestellungDao.findAll() : new ArrayList<Bestellung>();
	}

}
