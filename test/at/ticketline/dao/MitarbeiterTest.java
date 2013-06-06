package at.ticketline.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.service.api.MitarbeiterService;
import at.ticketline.service.impl.MitarbeiterServiceImpl;
import at.ticketline.test.AbstractDaoTest;

/**
 * This test demonstrates the usage of AbstractDaoTest
 * 
 * @author Rafael Konlechner
 */
public class MitarbeiterTest extends AbstractDaoTest {
	private MitarbeiterDao mitarbeiterDao;
	private MitarbeiterService mitarbeiterService;
	
	
	@Test
	public void testIfNewMitarbeiterPersistsWithoutException() {
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(
		        Mitarbeiter.class);
		Mitarbeiter m = new Mitarbeiter();

		m.setUsername("bernd");
		m.setVorname("Bernd");
		m.setNachname("Artmüller");
		m.setPasswort("geheim");
		m.setGeschlecht(Geschlecht.MAENNLICH);
		
		try {
			this.mitarbeiterDao.persist(m);
		}
		catch (ConstraintViolationException cve) {
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				//violations.add(cv.getPropertyPath().toString());
				System.out.println( cv.getPropertyPath().toString() );
			}
		}
	}
	
	@Test
	public void testGetMitarbeiterByUsername() {
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(
		        Mitarbeiter.class);
		Mitarbeiter m = new Mitarbeiter();

		m.setUsername("bernd");
		m.setVorname("Bernd");
		m.setNachname("Artmüller");
		m.setPasswort("geheim");
		m.setGeschlecht(Geschlecht.MAENNLICH);
		
		this.mitarbeiterDao.persist(m);
		
		assertEquals("bernd", 
		        this.mitarbeiterDao.findByUsername("bernd").getUsername());
	}
	
	@Test
	public void testSuccessfulMitarbeiterLogin() {
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(
		        Mitarbeiter.class);
		this.mitarbeiterService = new MitarbeiterServiceImpl(
		        this.mitarbeiterDao);
		Mitarbeiter m = new Mitarbeiter();

		m.setUsername("bernd");
		m.setVorname("Bernd");
		m.setNachname("Artmüller");
		m.setPasswort("geheim");
		m.setGeschlecht(Geschlecht.MAENNLICH);
		
		this.mitarbeiterDao.persist(m);
		
		assertTrue(this.mitarbeiterService.login("bernd", "geheim") != null);
	}
}