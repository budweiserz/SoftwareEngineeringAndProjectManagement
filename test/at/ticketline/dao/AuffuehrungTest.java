package at.ticketline.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class AuffuehrungTest extends AbstractDaoTest {
    private AuffuehrungDao auffuehrungDao;
    private Auffuehrung a;
    private Auffuehrung query;
    
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
    public void testFindByWithValidEntity() {
        this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        HashSet<Platz> plaetze = new HashSet<Platz>();
        plaetze.add(EntityGenerator.getValidPlatz(12));
        plaetze.add(EntityGenerator.getValidPlatz(14));
        plaetze.add(EntityGenerator.getValidPlatz(15));
        Date date = new Date();
        
        a = new Auffuehrung();
        a.setDatumuhrzeit(date);
        a.setHinweis("Der einschlaegigste Schlager-schlagabtausch seit es geschlagenen Schlagobers gibt!");
        //a.setVeranstaltung(EntityGenerator.getValidVeranstaltung(3));
        a.setId(14);
        a.setPreis(PreisKategorie.STANDARDPREIS);
        a.setPlaetze(plaetze);
        a.setStorniert(false);
        //a.setSaal(EntityGenerator.getValidSaal(5));
        
        query = new Auffuehrung();
        query.setPreis(PreisKategorie.STANDARDPREIS);
        query.setDatumuhrzeit(date);
        
        this.auffuehrungDao.persist(a);
        
        
        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query);
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

        List<Auffuehrung> results = this.auffuehrungDao.findByAuffuehrung(query);
        assertTrue(results.isEmpty());
    }
}
