package at.ticketline.dao;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;
import at.ticketline.test.TestInitializer;

public class OrtTest extends AbstractDaoTest {

	private static OrtDao ortDao;
	
	@BeforeClass
	public static void init() {
		TestInitializer.init();
		ortDao = (OrtDao)DaoFactory.getByEntity(Ort.class);		
	}
	
	@Test(expected = DaoException.class)
	public void findByOrt_NullArgument() {
		ortDao.findByOrt(null);
	}
	
	@Test
	public void findByOrt_withBezeichnung() {
		Ort o = EntityGenerator.getValidOrt(0);
		o.setBezeichnung("Bezeichnung");
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setBezeichnung("Bezeichnung");
		assertEquals(ortDao.findByOrt(query).size(), 1);
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
		assertEquals(ortDao.findByOrt(query).size(), 1);
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
		assertEquals(ortDao.findByOrt(query).size(), 1);
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
		assertEquals(ortDao.findByOrt(query).size(), 1);
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
		assertEquals(ortDao.findByOrt(query).size(), 1);
	}
	
	@Test
	public void findByOrt_withTyp() {
		Ort o = EntityGenerator.getValidOrt(0);
		o.setOrtstyp(Ortstyp.KINO);
		ortDao.persist(o);
		
		Ort query = new Ort();
		query.setOrtstyp(Ortstyp.KINO);
		assertEquals(ortDao.findByOrt(query).size(), 1);
	}
}
