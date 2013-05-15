package at.ticketline.service;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.KategorieDao;
import at.ticketline.entity.Kategorie;
import at.ticketline.service.api.KategorieService;
import at.ticketline.service.impl.KategorieServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class KategorieTest extends AbstractDaoTest {

	private static KategorieDao kategorieDao;
	private static KategorieService kategorieService;

	@BeforeClass
	public static void init() {

		kategorieDao = (KategorieDao)DaoFactory.getByEntity(Kategorie.class);
		kategorieService = new KategorieServiceImpl(kategorieDao);

	}

	@Test
	public void listAllKunden() {
		kategorieDao.persist(EntityGenerator.getValidKategorie(1));
		kategorieDao.persist(EntityGenerator.getValidKategorie(2));
		kategorieDao.persist(EntityGenerator.getValidKategorie(3));
		assertTrue(kategorieService.findAll().size() == 3);
	}

	@Test
	public void listAllKategorieIs0() {
		assertTrue(kategorieService.findAll().size() == 0);		
	}

}
