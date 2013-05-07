package at.ticketline.dao;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;


import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.test.AbstractDaoTest;

/**
 * This test demonstrates the usage of AbstractDaoTest
 * 
 * @author Rafael Konlechner
 */
public class MitarbeiterTest extends AbstractDaoTest {
	private MitarbeiterDao mitarbeiterDao;
	
	@Test
	public void testIfNewMitarbeiterPersistsWithoutException() {
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
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
			List<String> violations = new ArrayList<String>();
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				//violations.add(cv.getPropertyPath().toString());
				System.out.println( cv.getPropertyPath().toString() );
			}
		}
	}
	
	@Test
	public void testGetMitarbeiterByUsername() {
		this.mitarbeiterDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
		Mitarbeiter m = new Mitarbeiter();

		m.setUsername("bernd");
		m.setVorname("Bernd");
		m.setNachname("Artmüller");
		m.setPasswort("geheim");
		m.setGeschlecht(Geschlecht.MAENNLICH);
		
		this.mitarbeiterDao.persist(m);
		
		assertEquals("bernd", this.mitarbeiterDao.findByUsername("bernd").getUsername());

	}
}