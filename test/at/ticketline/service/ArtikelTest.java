package at.ticketline.service;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.dao.jpa.ArtikelDaoJpa;
import at.ticketline.entity.Artikel;
import at.ticketline.service.api.ArtikelService;
import at.ticketline.service.impl.ArtikelServiceImpl;
import at.ticketline.test.AbstractDaoTest;

public class ArtikelTest extends AbstractDaoTest {

	private static ArtikelDao artikelDao;
	private static ArtikelService artikelService;
	
	@BeforeClass
	public static void init() {
		artikelDao = (ArtikelDao)DaoFactory.getByEntity(Artikel.class);
		artikelService = new ArtikelServiceImpl(artikelDao);
	}
	
	@Test
	public void testFindAllShouldReturnEmptyList() {
		assertEquals(0, artikelService.findAll().size());
	}
	
	@Test
	public void testFindAllOnEmptyDatabase_shouldNotReturnNull() {
		assertNotNull(artikelService.findAll());
	}
}
