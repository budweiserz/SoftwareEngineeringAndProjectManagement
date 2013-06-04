package at.ticketline.service;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.service.api.OrtService;
import at.ticketline.service.impl.OrtServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class OrtTest extends AbstractDaoTest {
	
	private static OrtDao ortDao;
	private static OrtService ortService;

	@BeforeClass
    public static void init() {

        ortDao = (OrtDao) DaoFactory.getByEntity(Ort.class);
        ortService = new OrtServiceImpl(ortDao);
    }

	@Test(expected = ServiceException.class)
	public void findByOrt_NullArgument() {
		ortService.findByOrt(null);
	}
	
	@Test
	public void findByOrt_withBezeichnung() {
		Ort o = EntityGenerator.getValidOrt(0);
		o.setBezeichnung("Bezeichnung");
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setBezeichnung("Bezeichnung");
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withStraße() {
		Ort o = EntityGenerator.getValidOrt(0);
		Adresse adresse = new Adresse();
		adresse.setStrasse("Straße");
		o.setAdresse(adresse);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setAdresse(adresse);
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withOrt() {
		Ort o = EntityGenerator.getValidOrt(0);
		Adresse adresse = new Adresse();
		adresse.setOrt("Ort");
		o.setAdresse(adresse);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setAdresse(adresse);
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withPlz() {
		Ort o = EntityGenerator.getValidOrt(0);
		Adresse adresse = new Adresse();
		adresse.setPlz("1111");
		o.setAdresse(adresse);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setAdresse(adresse);
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withLand() {
		Ort o = EntityGenerator.getValidOrt(0);
		Adresse adresse = new Adresse();
		adresse.setLand("Land");
		o.setAdresse(adresse);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setAdresse(adresse);
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withTyp() {
		Ort o = EntityGenerator.getValidOrt(0);
		o.setOrtstyp(Ortstyp.KINO);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setOrtstyp(Ortstyp.KINO);
		assertEquals(ortService.findByOrt(query).size(), 1);
	}
}
