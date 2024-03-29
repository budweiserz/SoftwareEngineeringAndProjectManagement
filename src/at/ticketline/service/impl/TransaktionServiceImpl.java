package at.ticketline.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.TransaktionService;

/**
 * Service zum reservieren, verkaufen und stornieren.
 * 
 * @author Florian Klampfer
 */
public class TransaktionServiceImpl implements TransaktionService {

	private TransaktionDao transaktionDao;
	private MitarbeiterDao mitarbeiterDao;
	private AuffuehrungDao auffuehrungDao;
	private KundeDao kundeDao;

	public TransaktionServiceImpl(TransaktionDao transaktionDao) {
		this.transaktionDao = transaktionDao;
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
		this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		this.kundeDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
	}

	/**
	 * Reserviert Plätze zu einer Veranstaltung für einen neuen, bestehenden oder anonymen Kunden.
	 * 
	 * @param kunde Ein bestehender oder neuer kunde, null wenn anonymer Kunde
	 * @param auffuehrung Aufführung für die reserviert werden soll
	 * @param plaetze Ein Set von Plätzen die reserviert werden soll. 
	 *        Die Plätze müssen aus getPlaetze() der Aufführung stammen!
	 * @return Eine Transaktion welche die Reservierungsnummer beinhaltet
	 */
	@Override
	public Transaktion reserve(Mitarbeiter m, Kunde k, Auffuehrung a, Set<Platz> ps) {
		return execute(m, k, a, ps, Transaktionsstatus.RESERVIERUNG, PlatzStatus.RESERVIERT);
	}

	/**
	 * Verkauft Plätze zu einer Veranstaltung für einen neuen, bestehenden oder anonymen Kunden.
	 * 
	 * @param kunde Ein bestehender oder neuer kunde, null wenn anonymer Kunde
	 * @param auffuehrung Aufführung für die reserviert werden soll
	 * @param plaetze Ein Set von Plätzen die reserviert werden soll. 
	 *        Die Plätze müssen aus getPlaetze() der Aufführung stammen!
	 * @return Eine Transaktion welche die Zahlungsinformation beinhaltet
	 */
	@Override
	public Transaktion sell(Mitarbeiter m, Kunde k, Auffuehrung a, Set<Platz> ps) {
		return execute(m, k, a, ps, Transaktionsstatus.BUCHUNG, PlatzStatus.GEBUCHT);
	}
	
	
	private Transaktion execute(Mitarbeiter m, Kunde k, Auffuehrung a, Set<Platz> ps, Transaktionsstatus tstat, PlatzStatus pstat) {
		Transaktion t = new Transaktion();
		t.setDatumuhrzeit(new Date());
		t.setStatus(tstat);

		if(k != null) {
		    if(k.getPunkte() == null) {
		        k.setPunkte(new BigDecimal(0));
		    }
		    k.setPunkte(k.getPunkte().add(new BigDecimal(1))); // Punkte + 1;
			t.setKunde(k);
		}

		t.setMitarbeiter(m);
		t.setPlaetze(ps);

		for(Platz p : ps) {
			p.setStatus(pstat);
			p.setAuffuehrung(a);
			p.setTransaktion(t);
		}

		EntityManagerUtil.beginTransaction();

		transaktionDao.persist(t);

		if(m != null) { // should never be null 
    		m.getTransaktionen().add(t);
    		mitarbeiterDao.merge(m);
		}
		
		// anonymous kunde?
		if(k != null) {
    		k.getTransaktionen().add(t);
    		kundeDao.merge(k);
		}
		
		a.getPlaetze().addAll(ps);
		auffuehrungDao.merge(a);

		EntityManagerUtil.commitTransaction();

		return t;
	}

	@Override
	public void cancelReservation(Integer reservierungsNr) {
		Transaktion t = transaktionDao.findById(reservierungsNr);
		if (t != null) {
			t.setStatus(Transaktionsstatus.STORNO);
			Set<Platz> plaetze = new HashSet<Platz>(t.getPlaetze());
			
			if(plaetze.size() > 0) {
			    //Auffuehrung a = plaetze.iterator().next().getAuffuehrung();
				//a.getPlaetze().removeAll(t.getPlaetze());

    			for (Platz p : plaetze) {
    				p.setStatus(PlatzStatus.FREI);
    			}
			}

			t.setPlaetze(plaetze);
			transaktionDao.merge(t);
		}
	}

	@Override
	public void cancelTransaktion(Kunde k, Auffuehrung a) {
		for (Transaktion t : transaktionDao.findAll()) {
			if (k != null && k.equals(t.getKunde())) {

				Boolean persist = false;

				for (Platz p : t.getPlaetze()) {
					if (a != null && a.equals(p.getAuffuehrung())) {
						persist = true;
						t.setStatus(Transaktionsstatus.STORNO);
						p.setStatus(PlatzStatus.FREI);
					}
				}
				
				//a.getPlaetze().removeAll(t.getPlaetze());

				if (persist) {
					transaktionDao.merge(t);
					
					if (k.getPunkte().intValue() > 0) {
					    k.setPunkte(k.getPunkte().subtract(new BigDecimal(1))); // Punkte - 1
					}
					kundeDao.merge(k);
				}
			}
		}
	}

	@Override
	public List<Transaktion> find(Transaktion query, List<Veranstaltung> vs) {
    	if (query == null) {
    		throw new IllegalArgumentException("Der Suchparameter darf nicht null sein");
    	}

    	List<Transaktion> ts = transaktionDao.findByTransaktion(query);
    	Set<Transaktion> toRemove = new HashSet<Transaktion>();
        if (query.getKunde() != null) {
    		for (Transaktion t : ts) {
    		    if (vs != null && vs.size() > 0 && t.getPlaetze() != null && t.getPlaetze().iterator().hasNext()) {
    		        if (!vs.contains(t.getPlaetze().iterator().next().getAuffuehrung().getVeranstaltung())) {
			            toRemove.add(t);
    		        }
    		    }
    		}

    		ts.removeAll(toRemove);
    	}
        return ts;
	}
}
