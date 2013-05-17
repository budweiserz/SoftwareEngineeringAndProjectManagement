package at.ticketline.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.VeranstaltungService;
import at.ticketline.service.impl.VeranstaltungServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class VeranstaltungTest extends AbstractDaoTest{
	private VeranstaltungDao veranstaltungDao;
	private VeranstaltungService veranstaltungsService;

	@Test
	public void testIfNewVeranstaltungPersistsWithoutException() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		Veranstaltung v = new Veranstaltung();

		v.setBezeichnung("TestBezeichnung");
		v.setKategorie("TestKategorie");
		v.setDauer(9999);

		try {
			this.veranstaltungDao.persist(v);
		}
		catch (ConstraintViolationException cve) {
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				//violations.add(cv.getPropertyPath().toString());
				System.out.println( cv.getPropertyPath().toString() );
			}
		}
	}

	@Test
	public void testIfFindByVeranstaltungReturnsCorrectVeranstaltung() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		this.veranstaltungsService = new VeranstaltungServiceImpl(this.veranstaltungDao);

		List<Veranstaltung> result;

		Veranstaltung v1 = new Veranstaltung();
		v1.setBezeichnung("Frequency 2013");
		v1.setKategorie("TestKategorie");
		v1.setDauer(9999);

		Veranstaltung v2 = new Veranstaltung();
		v2.setBezeichnung("Wacken 2013");
		v2.setKategorie("TestKategorie2");
		v2.setDauer(1111);

		Veranstaltung v3 = new Veranstaltung();
		v3.setBezeichnung("Nova Rock 2013");
		v3.setKategorie("blabla");
		v3.setDauer(1);

		Veranstaltung filter = new Veranstaltung();
		filter.setBezeichnung("*2013*");
		filter.setDauer(1);

		try {
			// zuerst neue Veranstaltungen hinzufügen
			this.veranstaltungDao.persist(v1);
			this.veranstaltungDao.persist(v2);
			this.veranstaltungDao.persist(v3);

			result = this.veranstaltungsService.find(filter, null, null);

			assertEquals(3, result.size());
		}
		catch (ConstraintViolationException cve) {
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				//violations.add(cv.getPropertyPath().toString());
				System.out.println( cv.getPropertyPath().toString() );
			}
		}
	}

	@Test
	public void testFindByWithMinMaxDauer() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		for (int i = 0; i < 10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			v.setDauer(i);
			veranstaltungDao.persist(v);
		}
		
		Veranstaltung v = new Veranstaltung();
		
		List<Veranstaltung> found = veranstaltungDao.findByVeranstaltung(v, 3, 6);
		assertTrue(found.size() == 4);
	}
	
	@Test
	public void testFindByWithMinMaxDauer_shouldIgnoreDauerOfQueryObject() {
		this.veranstaltungDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		for (int i = 0; i < 10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			v.setDauer(i);
			veranstaltungDao.persist(v);
		}
		
		Veranstaltung v = new Veranstaltung();
		v.setDauer(10);
		
		List<Veranstaltung> found = veranstaltungDao.findByVeranstaltung(v, 3, 6);
		assertTrue(found.size() == 4);
	}
}
