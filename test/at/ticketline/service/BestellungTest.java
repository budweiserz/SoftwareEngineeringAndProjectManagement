package at.ticketline.service;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.BestellungDao;
import at.ticketline.dao.jpa.BestellungDaoJpa;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;
import at.ticketline.service.api.BestellungService;
import at.ticketline.service.impl.BestellungServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class BestellungTest extends AbstractDaoTest {

	private static BestellungService bestellungService;
	private static BestellungDao bestellungDao;

	@BeforeClass
	public static void init() {
		bestellungDao = (BestellungDao)DaoFactory.getByEntity(Bestellung.class);
		bestellungService = new BestellungServiceImpl(bestellungDao);
	}

	@Test
	public void testEmptyDatabaseShouldReturnNoBestellungen() {
		assertEquals(0, bestellungService.findAll().size());
	}

	@Test
	public void testEmtpyDatabaseShouldNotReturnNull() {
		assertNotNull(bestellungService.findAll());
	}

	@Test
	public void testSaveBestellungWithArtikelHavingAmountZero_ShouldNotSaveParticularBestellung() {
		assertEquals(0, bestellungService.findAll().size());

		Artikel a = EntityGenerator.getValidArtikel(0);
		HashMap<Artikel, Integer> bestellungen = new HashMap<Artikel, Integer>();
		bestellungen.put(a, 0);
		bestellungService.saveBestellungen(bestellungen);

		assertEquals(0, bestellungService.findAll().size());
	}

	@Test
	public void testSaveBestellungWithSomeHavingAmountZero_shouldNotSaveThoseParticularArtikel() {
		assertEquals(0, bestellungService.findAll().size());

		Artikel a1 = EntityGenerator.getValidArtikel(0);
		Artikel a2 = EntityGenerator.getValidArtikel(1);
		Artikel a3 = EntityGenerator.getValidArtikel(2);
		Artikel a4 = EntityGenerator.getValidArtikel(3);

		HashMap<Artikel, Integer> bestellungen = new HashMap<Artikel, Integer>();
		bestellungen.put(a1, 3);
		bestellungen.put(a2, 0);
		bestellungen.put(a3, 2);
		bestellungen.put(a4, 0);
		bestellungService.saveBestellungen(bestellungen);

		assertEquals(1, bestellungService.findAll().size());

		Bestellung b = bestellungService.findAll().get(0);

		assertEquals(2, b.getBestellPositionen().size());

	}

	@Test
	public void testSaveSingleBestellung_shouldWork() {
		assertEquals(0, bestellungService.findAll().size());

		Artikel a1 = EntityGenerator.getValidArtikel(0);
		Artikel a2 = EntityGenerator.getValidArtikel(1);
		Artikel a3 = EntityGenerator.getValidArtikel(2);
		Artikel a4 = EntityGenerator.getValidArtikel(3);

		HashMap<Artikel, Integer> bestellungen = new HashMap<Artikel, Integer>();
		bestellungen.put(a1, 3);
		bestellungen.put(a2, 2);
		bestellungen.put(a3, 1);
		bestellungen.put(a4, 4);
		bestellungService.saveBestellungen(bestellungen);

		assertEquals(1, bestellungService.findAll().size());

		Bestellung b = bestellungService.findAll().get(0);

		assertEquals(4, b.getBestellPositionen().size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSaveBestellungWithNullArgument_shouldThrowIllegalArgumentException() {
		bestellungService.saveBestellungen(null);
	}

}
