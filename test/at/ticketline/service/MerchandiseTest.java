package at.ticketline.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.MerchandiseDao;
import at.ticketline.entity.Merchandise;
import at.ticketline.service.api.MerchandiseService;
import at.ticketline.service.impl.MerchandiseServiceImpl;
import at.ticketline.test.AbstractDaoTest;

public class MerchandiseTest extends AbstractDaoTest {

	private static MerchandiseDao MerchandiseDao;
	private static MerchandiseService MerchandiseService;
	
	@BeforeClass
	public static void init() {
		MerchandiseDao = (MerchandiseDao)DaoFactory.getByEntity(Merchandise.class);
		MerchandiseService = new MerchandiseServiceImpl(MerchandiseDao);
	}
	
	@Test
	public void testFindAllShouldReturnEmptyList() {
		assertEquals(0, MerchandiseService.findAll().size());
	}
	
	@Test
	public void testFindAllOnEmptyDatabase_shouldNotReturnNull() {
		assertNotNull(MerchandiseService.findAll());
	}
}