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
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
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
    private static KundeDao kundeDao;
    private static AuffuehrungDao auffuehrungDao ;
    private static SaalDao saalDao;
    private static VeranstaltungDao veranstaltungDao;
    private static MitarbeiterDao mitarbeiterDao;
    private static TransaktionDao dao;
    private static TransaktionService service;
    
    private static Kunde k;
    private static Auffuehrung a;
    private static Veranstaltung v;
    private static Saal s;
    private static Set<Platz> ps;
    private static Mitarbeiter m;
    
    @BeforeClass
    public static void init() {
        kundeDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
        auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
        
        dao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
        service = new TransaktionServiceImpl(dao);
    }
    
    @Before
    public void simpleSetup() {
        k = EntityGenerator.getValidKunde(0);
        m = EntityGenerator.getValidMitarbeiter(0);
        a = EntityGenerator.getValidAuffuehrung(0);
        v = EntityGenerator.getValidVeranstaltung(0);
        s = EntityGenerator.getValidSaal(0);
        
        ps = new LinkedHashSet<Platz>();
        Platz p;
        for(int i=0; i<10; i++) {
            p = EntityGenerator.getValidPlatz(i);
            ps.add(p);
        }
        
        a.setVeranstaltung(v);
        a.setPlaetze(ps);
        a.setSaal(s);
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