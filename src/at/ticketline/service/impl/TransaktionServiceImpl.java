package at.ticketline.service.impl;

import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.ReiheDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.TransaktionService;

/**
 * Service zum reservieren, verkaufen und stornieren.
 * 
 * @author Florian Klampfer
 */
public class TransaktionServiceImpl implements TransaktionService {
    
    private TransaktionDao transaktionDao;
    private MitarbeiterDao mitarbeiterDao;
    private KundeDao kundeDao;
    
    public TransaktionServiceImpl() {
        this.transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
        this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
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
        return execute(m, k, a, ps, null, Transaktionsstatus.RESERVIERUNG, PlatzStatus.RESERVIERT);
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
    public Transaktion sell(Mitarbeiter m, Kunde k, Auffuehrung a, Set<Platz> ps, Zahlungsart z) {
        return execute(m, k, a, ps, z, Transaktionsstatus.BUCHUNG, PlatzStatus.GEBUCHT);
    }
    
    private Transaktion execute(Mitarbeiter m, Kunde k, Auffuehrung a, Set<Platz> ps, Zahlungsart z, Transaktionsstatus tstat, PlatzStatus pstat) {
        Transaktion t = new Transaktion();
        t.setDatumuhrzeit(new Date());
        t.setStatus(tstat);
        
        if(k != null) {
            t.setKunde(k);
        }
        
        t.setMitarbeiter(m);
        t.setPlaetze(ps);
        
        for(Platz p : ps) {
            p.setStatus(pstat);
            p.setAuffuehrung(a);
            p.setTransaktion(t);
        }
        
        if (z != null) {
            t.setZahlungsart(z);
        }
        
        EntityManagerUtil.beginTransaction();
        
        transaktionDao.persist(t);
        
        m.getTransaktionen().add(t);
        mitarbeiterDao.persist(m);
        
        k.getTransaktionen().add(t);
        kundeDao.persist(k);
        
        EntityManagerUtil.commitTransaction();
        
        return t;
    }

    @Override
    public void cancel(Integer reservierungsNr) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void cancel(Kunde k, Auffuehrung a) {
        // TODO Auto-generated method stub
        
    }
}
