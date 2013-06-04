package at.ticketline.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.VeranstaltungService;
import at.ticketline.service.impl.VeranstaltungServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class VeranstaltungTest extends AbstractDaoTest {
	
	private VeranstaltungDao veranstaltungDao;
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

}
