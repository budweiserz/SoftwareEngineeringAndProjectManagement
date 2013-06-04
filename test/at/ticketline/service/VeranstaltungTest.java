package at.ticketline.service;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.VeranstaltungService;
import at.ticketline.service.impl.VeranstaltungServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class VeranstaltungTest extends AbstractDaoTest {
	
	private VeranstaltungDao veranstaltungDao;
	private AuffuehrungDao auffuehrungDao;

	private VeranstaltungService veranstaltungService;

	@Test
	public void testFindByWithMinMaxDauer() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		this.veranstaltungService = new VeranstaltungServiceImpl(veranstaltungDao);
		for (int i = 0; i < 10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			v.setDauer(i);
			veranstaltungDao.persist(v);
		}
		
		Veranstaltung v = new Veranstaltung();
		
		List<Veranstaltung> found = veranstaltungService.find(v, 3, 6);
		assertTrue(found.size() == 4);
	}
	
	@Test
	public void testFindByWithMinMaxDauer_shouldIgnoreDauerOfQueryObject() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		this.veranstaltungService = new VeranstaltungServiceImpl(veranstaltungDao);
		for (int i = 0; i < 10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			v.setDauer(i);
			veranstaltungDao.persist(v);
		}
		
		Veranstaltung v = new Veranstaltung();
		v.setDauer(9);
		
		List<Veranstaltung> found = veranstaltungService.find(v, 3, 6);
		assertTrue(found.size() == 4);
	}
	
	@Test
	public void testIfTopTenVeranstaltungenReturnsCorrectTopTenVeranstaltungen() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		this.auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		
		this.veranstaltungService = new VeranstaltungServiceImpl(this.veranstaltungDao);
		Set<Auffuehrung> auffuehrungen = new HashSet<Auffuehrung>();
		
		// Testdaten erstellen
		for (int i = 0; i < 10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			auffuehrungen = new HashSet<Auffuehrung>();
			
			v = veranstaltungDao.persist(v);
			
			int rand2 = new Random().nextInt(20);

			for (int k = 0; k < rand2; k++) {
				Auffuehrung a = EntityGenerator.getValidAuffuehrung(Integer.parseInt(i + "" + k));
				
				int rand = new Random().nextInt(50);
				Set<Platz> plaetze = new HashSet<Platz>();
				Platz p;
				
				for (int j = 0; j < rand; j++) {
					p = EntityGenerator.getValidPlatz(Integer.parseInt(k + "" + j));
					p.setStatus(PlatzStatus.GEBUCHT);
					
					plaetze.add(p);
				}
				a.setPlaetze(plaetze);
				a.setVeranstaltung(v);
				a = this.auffuehrungDao.persist(a);
				auffuehrungen.add(a);
			}
			
			v.setAuffuehrungen(auffuehrungen);
			v = veranstaltungDao.persist(v);
			
		}
		Calendar c = new GregorianCalendar();
		c.set(2012, 6, 5);
		Date start = c.getTime();
		c.set(2013, 6, 5);
		Date end = c.getTime();
		
		HashMap<Veranstaltung, Integer> topTen = this.veranstaltungService.findTopTen(start, end, null);
		
		Iterator<Map.Entry<Veranstaltung, Integer>> it = topTen.entrySet().iterator();
		while( it.hasNext()) {
			System.out.println( it.next().getValue() );
		}
	}
}
