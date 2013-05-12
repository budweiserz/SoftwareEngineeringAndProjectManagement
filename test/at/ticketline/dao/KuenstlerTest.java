package at.ticketline.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.KuenstlerService;
import at.ticketline.service.impl.KuenstlerServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;
import at.ticketline.test.TestInitializer;

public class KuenstlerTest extends AbstractDaoTest {

	private static KuenstlerDao kuenstlerDao;
	private static KuenstlerService kuenstlerService;

	@BeforeClass
	public static void init() {
		TestInitializer.init();
		kuenstlerDao = (KuenstlerDao)DaoFactory.getByEntity(Kuenstler.class);
		kuenstlerService = new KuenstlerServiceImpl(kuenstlerDao);
	}

	@Test
	public void testPersistKuenstler() {

		Kuenstler k = new Kuenstler();

		try {
			kuenstlerDao.persist(k);
			fail("Künstler konnte trotz Fehlern persistiert werden");
		}
		catch (ConstraintViolationException cve) {
			List<String> violations = new ArrayList<String>();
			for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
				violations.add(cv.getPropertyPath().toString());
			}
			assertEquals(3, violations.size());
			assertTrue(violations.contains("nachname"));
			assertTrue(violations.contains("vorname"));
			assertTrue(violations.contains("geschlecht"));
		}
	}

	@Test
	public void testFindByKuenstler_shouldWork() {

		ArrayList<Kuenstler> kuenstler = new ArrayList<Kuenstler>();

		for(int i = 0; i < 100; i++) {
			Kuenstler k = EntityGenerator.getValidKuenstler(i);
			kuenstler.add(k);
			kuenstlerDao.persist(k);
		}

		for (Kuenstler k : kuenstler) {
			List<Kuenstler> found = kuenstlerDao.findByKuenstler(k);
			if (found.size() == 0) {
				fail("Ein existierender Künstler konnte nicht gefunden werden");
			}
			assertEquals(k.getVorname(), found.get(0).getVorname());
			assertEquals(k.getNachname(), found.get(0).getNachname());
			assertEquals(k.getGeschlecht(), found.get(0).getGeschlecht());
		}

	}

	@Test
	public void testFindByKuenstlerVorname_shouldWork() {

		for (int i = 0; i < 100; i++) {

			Kuenstler k = new Kuenstler();
			k.setNachname("Nachname " + i);
			k.setVorname("Vorname");
			k.setTitel("Titel " + i);
			k.setGeschlecht(Geschlecht.WEIBLICH);
			k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
			k.setBiographie("Biographie " + i);
			kuenstlerDao.persist(k);
		}

		Kuenstler query = new Kuenstler();
		query.setVorname("Vorname");
		List<Kuenstler> kuenstler = kuenstlerDao.findByKuenstler(query);
		assertEquals(kuenstler.size(), 100);
		
	}

	@Test
	public void testFindByKuenstlerNachname_shouldWork() {

		for (int i = 0; i < 100; i++) {

			Kuenstler k = new Kuenstler();
			k.setNachname("Nachname");
			k.setVorname("Vorname " + i);
			k.setTitel("Titel " + i);
			k.setGeschlecht(Geschlecht.WEIBLICH);
			k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
			k.setBiographie("Biographie " + i);
			kuenstlerDao.persist(k);
		}

		Kuenstler query = new Kuenstler();
		query.setNachname("Nachname");
		List<Kuenstler> kuenstler = kuenstlerDao.findByKuenstler(query);
		assertEquals(kuenstler.size(), 100);
	}

	@Test(expected = DaoException.class)
	public void testFindByKuenstler_nullArgument() {
		kuenstlerDao = (KuenstlerDao)DaoFactory.getByEntity(Kuenstler.class);
		kuenstlerDao.findByKuenstler(null);
	}
	
	@Test(expected = ServiceException.class)
    public void findByKuenstlerNull_shouldThrowException() {

        kuenstlerService.findByKuenstler(null);
    }
	
	@Test(expected = ServiceException.class)
    public void findByKuenstlerWithAnySexNull_shouldThrowException() {

        kuenstlerService.findByKuenstlerWithAnySex(null);
    }

	@Test
	public void testFindByKuenstlerWithAnySex() {

		for (int i = 0; i < 100; i++) {

			Kuenstler k = new Kuenstler();
			k.setNachname("Nachname " + i);
			k.setVorname("Vorname");
			k.setTitel("Titel " + i);
			k.setGeschlecht((i % 2) == 0 ? Geschlecht.WEIBLICH : Geschlecht.MAENNLICH);
			k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
			k.setBiographie("Biographie " + i);
			kuenstlerDao.persist(k);
		}
		
		Kuenstler query = new Kuenstler();
		query.setVorname("Vorname");
		List<Kuenstler> kuenstler = kuenstlerDao.findByKuenstler(query);
		assertEquals(kuenstler.size(), 100);

	}
	
	@Test
	public void testFindByKuenstlerVornameViaService_shouldWork() {

		for (int i = 0; i < 100; i++) {

			Kuenstler k = new Kuenstler();
			k.setNachname("Nachname " + i);
			k.setVorname("Vorname");
			k.setTitel("Titel " + i);
			k.setGeschlecht(Geschlecht.WEIBLICH);
			k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
			k.setBiographie("Biographie " + i);
			kuenstlerDao.persist(k);
		}

		Kuenstler query = new Kuenstler();
		query.setVorname("Vorname");
		List<Kuenstler> kuenstler = kuenstlerService.findByKuenstler(query);
		assertEquals(kuenstler.size(), 100);
		
	}

	@Test
	public void testFindByKuenstlerNachnameViaService_shouldWork() {

		for (int i = 0; i < 100; i++) {

			Kuenstler k = new Kuenstler();
			k.setNachname("Nachname");
			k.setVorname("Vorname " + i);
			k.setTitel("Titel " + i);
			k.setGeschlecht(Geschlecht.WEIBLICH);
			k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
			k.setBiographie("Biographie " + i);
			kuenstlerDao.persist(k);
		}

		Kuenstler query = new Kuenstler();
		query.setNachname("Nachname");
		List<Kuenstler> kuenstler = kuenstlerService.findByKuenstler(query);
		assertEquals(kuenstler.size(), 100);
	}

}
