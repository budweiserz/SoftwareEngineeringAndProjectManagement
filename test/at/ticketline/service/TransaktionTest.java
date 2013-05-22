package at.ticketline.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.ReiheDao;
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.TransaktionService;
import at.ticketline.service.impl.TransaktionServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class TransaktionTest extends AbstractDaoTest {
    private static KundeDao kDao;
    private static AuffuehrungDao aDao ;
    private static SaalDao sDao;
    private static VeranstaltungDao vDao;
    private static MitarbeiterDao mDao;
    private static ReiheDao rDao;
    private static TransaktionDao tDao;
    private static TransaktionService service;
    
    private static Kunde k;
    private static Auffuehrung a;
    private static Veranstaltung v;
    private static Saal s;
    private static Reihe r;
    private static Set<Platz> ps;
    private static Set<Reihe> rs;
    private static Mitarbeiter m;
    
    @BeforeClass
    public static void init() {
        kDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
        aDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        sDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        vDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        mDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
        rDao = (ReiheDao)DaoFactory.getByEntity(Reihe.class);
        
        service = new TransaktionServiceImpl();
    }
    
    @Before
    public void simpleSetup() {
        k = EntityGenerator.getValidKunde(0);
        kDao.persist(k);
        m = EntityGenerator.getValidMitarbeiter(0);
        mDao.persist(m);
        a = EntityGenerator.getValidAuffuehrung(0);
        aDao.persist(a);
        v = EntityGenerator.getValidVeranstaltung(0);
        vDao.persist(v);
        s = EntityGenerator.getValidSaal(0);
        sDao.persist(s);
        r = EntityGenerator.getValidReihe(0);
        rDao.persist(r);
        
        ps = new LinkedHashSet<Platz>();
        Platz p;
        for(int i=0; i<10; i++) {
            p = EntityGenerator.getValidPlatz(i);
            p.setAuffuehrung(a);
            p.setReihe(r);
            ps.add(p);
        }
        
        r.setBelegungen(ps);
        r.setSaal(s);
        rDao.merge(r);
        
        rs = new LinkedHashSet<Reihe>();
        rs.add(r);
        s.setReihen(rs);
        sDao.merge(s);
        
        a.setVeranstaltung(v);
        a.setPlaetze(ps);
        a.setSaal(s);
        aDao.merge(a);
    }
    
    @Test
    public void testTestSetup() {
        assertTrue(k != null);
        assertTrue(a != null);
        assertTrue(v != null);
        assertTrue(ps != null);
        assertTrue(ps.size() > 0);
    }
    
    @Test
    public void reserve() {
        Set<Platz> xps = new LinkedHashSet<Platz>();
        
        Iterator<Platz> it = ps.iterator();
        for(int i=0; i<3; i++) {
            xps.add(it.next());
        }
        
        Transaktion t = service.reserve(m, k, a, xps);
        
        assertTrue(t != null);
        assertEquals(t.getStatus(), Transaktionsstatus.RESERVIERUNG);
        assertEquals(t.getKunde(), k);
        assertEquals(t.getPlaetze(), xps);
        assertEquals(t.getMitarbeiter(), m);
        assertTrue(t.getReservierungsnr() != null);
    }
    
    @Test
    public void sell() {
        Set<Platz> xps = new LinkedHashSet<Platz>();
        
        Iterator<Platz> it = ps.iterator();
        for(int i=0; i<3; i++) {
            xps.add(it.next());
        }
        
        Transaktion t = service.sell(m, k, a, xps, Zahlungsart.KREDITKARTE);
        
        assertTrue(t != null);
        assertEquals(t.getStatus(), Transaktionsstatus.BUCHUNG);
        assertEquals(t.getKunde(), k);
        assertEquals(t.getPlaetze(), xps);
        assertEquals(t.getMitarbeiter(), m);
        assertTrue(t.getReservierungsnr() != null);
    }
}
