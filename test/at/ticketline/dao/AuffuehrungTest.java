package at.ticketline.dao;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class AuffuehrungTest extends AbstractDaoTest {
    private AuffuehrungDao auffuehrungDao;
    private VeranstaltungDao veranstaltungDao;
    private SaalDao saalDao;
    private Auffuehrung a;
    private Auffuehrung query;
    private Auffuehrung query2;
    
    @Test
    public void testIfNewAuffuehrungPersistsWithoutException() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(2));
        plaetze.add(EntityGenerator.getValidPlatz(3));
        plaetze.add(EntityGenerator.getValidPlatz(4));


        a = new Auffuehrung();
        a.setDatumuhrzeit(new Date());
        a.setHinweis("Der rockigste Rockabend seit Nachmittag!");
        a.setVeranstaltung(EntityGenerator.getValidVeranstaltung(3));
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(EntityGenerator.getValidSaal(5));
        
        try {
            this.auffuehrungDao.persist(a);
        } catch (ConstraintViolationException cve) {
            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                //violations.add(cv.getPropertyPath().toString());
                System.out.println( cv.getPropertyPath().toString() );
            }
        }
    }
    
    @Test
    public void testFindByWithValidEntityAllCriteria() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Saal saal = EntityGenerator.getValidSaal(5);
        Veranstaltung veranstaltung = EntityGenerator.getValidVeranstaltung(3);

        this.saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        this.saalDao.persist(saal);
        this.veranstaltungDao.persist(veranstaltung);
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(new Date(1325422372));
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        a.setVeranstaltung(veranstaltung);
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(saal);
        
        query = new Auffuehrung();
        query.setPreis(PreisKategorie.STANDARDPREIS);
        query.setDatumuhrzeit(new Date(1293886372));
        query.setSaal(saal);
        query.setVeranstaltung(veranstaltung);
        
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        this.auffuehrungDao.persist(a);
        
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.contains(a));
    }
    
    @Test
    public void testFindByWithValidEntityByDate() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Date date = new Date(1325422372);
        Saal saal = EntityGenerator.getValidSaal(5);
        Veranstaltung veranstaltung = EntityGenerator.getValidVeranstaltung(3);

        this.saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        this.saalDao.persist(saal);
        this.veranstaltungDao.persist(veranstaltung);
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(date);
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        a.setVeranstaltung(veranstaltung);
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(saal);
        
        query = new Auffuehrung();
        query.setDatumuhrzeit(new Date(1230814372));
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        this.auffuehrungDao.persist(a);
        
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.contains(a));       
    }
    
    @Test
    public void testFindByWithValidEntityByPrice() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Date date = new Date();
        Saal saal = EntityGenerator.getValidSaal(5);
        Veranstaltung veranstaltung = EntityGenerator.getValidVeranstaltung(3);

        this.saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        this.saalDao.persist(saal);
        this.veranstaltungDao.persist(veranstaltung);
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(date);
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        a.setVeranstaltung(veranstaltung);
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(saal);
        
        query = new Auffuehrung();
        query.setPreis(PreisKategorie.STANDARDPREIS);
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        this.auffuehrungDao.persist(a);
       
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.contains(a));      
    }
    
    @Test
    public void testFindByWithValidEntityBySaal() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Date date = new Date();
        Saal saal = EntityGenerator.getValidSaal(5);
        Veranstaltung veranstaltung = EntityGenerator.getValidVeranstaltung(3);

        this.saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        this.saalDao.persist(saal);
        this.veranstaltungDao.persist(veranstaltung);
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(date);
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        a.setVeranstaltung(veranstaltung);
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(saal);
        
        query = new Auffuehrung();
        query.setSaal(saal);
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        this.auffuehrungDao.persist(a);
        
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.contains(a));       
    }
    
    @Test
    public void testFindByWithValidEntityByVeranstaltung() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Date date = new Date();
        Saal saal = EntityGenerator.getValidSaal(5);
        Veranstaltung veranstaltung = EntityGenerator.getValidVeranstaltung(3);

        this.saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
        this.saalDao.persist(saal);
        this.veranstaltungDao.persist(veranstaltung);
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(date);
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        a.setVeranstaltung(veranstaltung);
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        a.setSaal(saal);
        
        query = new Auffuehrung();
        query.setVeranstaltung(veranstaltung);
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        this.auffuehrungDao.persist(a);
        
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.contains(a));       
    }
    
    @Test
    public void testFindByWithNoResults() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        
        query = new Auffuehrung();
        query.setVeranstaltung(EntityGenerator.getValidVeranstaltung(13));
        query.setSaal(EntityGenerator.getValidSaal(5));
        query.setPreis(PreisKategorie.STANDARDPREIS);
        query.setDatumuhrzeit(new Date());
        query2 = new Auffuehrung();
        query2.setDatumuhrzeit(new Date(1388580772));
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query, query2);
        assertTrue(results.isEmpty());
    }
}
