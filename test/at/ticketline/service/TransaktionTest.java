package at.ticketline.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.ReiheDao;
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Transaktionsstatus;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.entity.Zahlungsart;
import at.ticketline.service.api.TransaktionService;
import at.ticketline.service.impl.TransaktionServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class TransaktionTest extends AbstractDaoTest {
	private static KundeDao kDao;
	private static AuffuehrungDao aDao ;
	private static SaalDao sDao;
	private static VeranstaltungDao vDao;
	private static MitarbeiterDao mDao;
	private static ReiheDao rDao;
	private static TransaktionDao tDao;
	private static TransaktionService service;

	private static Kunde k;
	private static Auffuehrung a;
	private static Veranstaltung v;
	private static Saal s;
	private static Reihe r;
	private static Set<Platz> ps;
	private static Set<Reihe> rs;
	private static Mitarbeiter m;

	@BeforeClass
	public static void init() {
		kDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		aDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		sDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
		vDao = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		mDao = (MitarbeiterDao)DaoFactory.getByEntity(Mitarbeiter.class);
		rDao = (ReiheDao)DaoFactory.getByEntity(Reihe.class);

		service = new TransaktionServiceImpl();
	}

	@Test
	public void testTestSetup() {
		k = EntityGenerator.getValidKunde(0);
		kDao.persist(k);
		m = EntityGenerator.getValidMitarbeiter(0);
		mDao.persist(m);
		a = EntityGenerator.getValidAuffuehrung(0);
		aDao.persist(a);
		v = EntityGenerator.getValidVeranstaltung(0);
		vDao.persist(v);
		s = EntityGenerator.getValidSaal(0);
		sDao.persist(s);
		r = EntityGenerator.getValidReihe(0);
		rDao.persist(r);

		ps = new LinkedHashSet<Platz>();
		Platz p;
		for(int i=0; i<10; i++) {
			p = EntityGenerator.getValidPlatz(i);
			p.setAuffuehrung(a);
			p.setReihe(r);
			ps.add(p);
		}

		r.setBelegungen(ps);
		r.setSaal(s);
		rDao.merge(r);

		rs = new LinkedHashSet<Reihe>();
		rs.add(r);
		s.setReihen(rs);
		sDao.merge(s);

		a.setVeranstaltung(v);
		a.setPlaetze(ps);
		a.setSaal(s);
		aDao.merge(a);

		assertTrue(k != null);
		assertTrue(a != null);
		assertTrue(v != null);
		assertTrue(ps != null);
		assertTrue(ps.size() > 0);
	}

	@Test
	public void reserve() {
		k = EntityGenerator.getValidKunde(0);
		kDao.persist(k);
		m = EntityGenerator.getValidMitarbeiter(0);
		mDao.persist(m);
		a = EntityGenerator.getValidAuffuehrung(0);
		aDao.persist(a);
		v = EntityGenerator.getValidVeranstaltung(0);
		vDao.persist(v);
		s = EntityGenerator.getValidSaal(0);
		sDao.persist(s);
		r = EntityGenerator.getValidReihe(0);
		rDao.persist(r);

		ps = new LinkedHashSet<Platz>();
		Platz p;
		for(int i=0; i<10; i++) {
			p = EntityGenerator.getValidPlatz(i);
			p.setAuffuehrung(a);
			p.setReihe(r);
			ps.add(p);
		}

		r.setBelegungen(ps);
		r.setSaal(s);
		rDao.merge(r);

		rs = new LinkedHashSet<Reihe>();
		rs.add(r);
		s.setReihen(rs);
		sDao.merge(s);

		a.setVeranstaltung(v);
		a.setPlaetze(ps);
		a.setSaal(s);
		aDao.merge(a);

		Set<Platz> xps = new LinkedHashSet<Platz>();

		Iterator<Platz> it = ps.iterator();
		for(int i=0; i<3; i++) {
			xps.add(it.next());
		}

		Transaktion t = service.reserve(m, k, a, xps);

		assertTrue(t != null);
		assertEquals(t.getStatus(), Transaktionsstatus.RESERVIERUNG);
		assertEquals(t.getKunde(), k);
		assertEquals(t.getPlaetze(), xps);
		assertEquals(t.getMitarbeiter(), m);
		assertTrue(t.getReservierungsnr() != null);
	}

	@Test
	public void sell() {
		k = EntityGenerator.getValidKunde(0);
		kDao.persist(k);
		m = EntityGenerator.getValidMitarbeiter(0);
		mDao.persist(m);
		a = EntityGenerator.getValidAuffuehrung(0);
		aDao.persist(a);
		v = EntityGenerator.getValidVeranstaltung(0);
		vDao.persist(v);
		s = EntityGenerator.getValidSaal(0);
		sDao.persist(s);
		r = EntityGenerator.getValidReihe(0);
		rDao.persist(r);

		ps = new LinkedHashSet<Platz>();
		Platz p;
		for(int i=0; i<10; i++) {
			p = EntityGenerator.getValidPlatz(i);
			p.setAuffuehrung(a);
			p.setReihe(r);
			ps.add(p);
		}

		r.setBelegungen(ps);
		r.setSaal(s);
		rDao.merge(r);

		rs = new LinkedHashSet<Reihe>();
		rs.add(r);
		s.setReihen(rs);
		sDao.merge(s);

		a.setVeranstaltung(v);
		a.setPlaetze(ps);
		a.setSaal(s);
		aDao.merge(a);

		Set<Platz> xps = new LinkedHashSet<Platz>();

		Iterator<Platz> it = ps.iterator();
		for(int i=0; i<3; i++) {
			xps.add(it.next());
		}

		Transaktion t = service.sell(m, k, a, xps, Zahlungsart.KREDITKARTE);

		assertTrue(t != null);
		assertEquals(t.getStatus(), Transaktionsstatus.BUCHUNG);
		assertEquals(t.getKunde(), k);
		assertEquals(t.getPlaetze(), xps);
		assertEquals(t.getMitarbeiter(), m);
		assertTrue(t.getReservierungsnr() != null);
	}

	@Test
	public void testCancelReservation() {

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t = new Transaktion();
		t.setDatumuhrzeit(new Date());
		t.setStatus(Transaktionsstatus.RESERVIERUNG);
		t.setZahlungsart(Zahlungsart.VORKASSE);
		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for (int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.RESERVIERT);
			plaetze.add(p);
		}
		t.setPlaetze(plaetze);

		transaktionDao.persist(t);

		assertEquals(1, transaktionDao.findAll().size());

		t = transaktionDao.findById(t.getId());

		assertEquals(Transaktionsstatus.RESERVIERUNG, t.getStatus());

		TransaktionService service = new TransaktionServiceImpl();

		service.cancelReservation(t.getId());

		t = transaktionDao.findById(t.getId());

		assertEquals(Transaktionsstatus.STORNO, t.getStatus());

		for (Platz p : t.getPlaetze()) {
			assertEquals(PlatzStatus.FREI, p.getStatus());
		}
	}
	
	@Test
	public void testCancelReservation_shouldNotCancelAnyReservationWithUnknownId() {

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t = new Transaktion();
		t.setDatumuhrzeit(new Date());
		t.setStatus(Transaktionsstatus.RESERVIERUNG);
		t.setZahlungsart(Zahlungsart.VORKASSE);
		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for (int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.RESERVIERT);
			plaetze.add(p);
		}
		t.setPlaetze(plaetze);

		transaktionDao.persist(t);

		assertEquals(1, transaktionDao.findAll().size());

		t = transaktionDao.findById(t.getId());

		assertEquals(Transaktionsstatus.RESERVIERUNG, t.getStatus());

		TransaktionService service = new TransaktionServiceImpl();

		service.cancelReservation(t.getId() + 39023);

		t = transaktionDao.findById(t.getId());

		assertEquals(Transaktionsstatus.RESERVIERUNG, t.getStatus());

		for (Platz p : t.getPlaetze()) {
			assertEquals(PlatzStatus.RESERVIERT, p.getStatus());
		}
	}

	@Test
	public void testCancelTransaktion() {
		KundeDao kundenDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		Kunde k = EntityGenerator.getValidKunde(0);
		kundenDao.persist(k);

		AuffuehrungDao auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		Auffuehrung a = new Auffuehrung();
		a.setDatumuhrzeit(new Date());
		a.setPreis(PreisKategorie.STANDARDPREIS);

		auffuehrungDao.persist(a);

		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a);
		}

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		TransaktionService service = new TransaktionServiceImpl();

		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t = new Transaktion();
		t.setDatumuhrzeit(new Date());
		t.setStatus(Transaktionsstatus.BUCHUNG);
		t.setZahlungsart(Zahlungsart.VORKASSE);
		t.setKunde(k);
		t.setPlaetze(plaetze);
		transaktionDao.persist(t);

		assertEquals(1, transaktionDao.findAll().size());

		service.cancelTransaktion(k, a);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k.equals(trans.getKunde())) {
				Boolean allFromOneAuffuehrung = true;
				for (Platz p : trans.getPlaetze()) {
					allFromOneAuffuehrung &= a.equals(p.getAuffuehrung());
				}

				assertTrue(allFromOneAuffuehrung);

				if (allFromOneAuffuehrung) {

					assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

					for (Platz p : trans.getPlaetze()) {
						assertEquals(PlatzStatus.FREI, p.getStatus());
					}
				}
			}
		}
	}

	@Test
	public void testCancelTransaktion_shouldOnlyCancelTranaktionWithAssociatedAuffuehrung() {
		KundeDao kundenDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		Kunde k = EntityGenerator.getValidKunde(0);
		kundenDao.persist(k);

		AuffuehrungDao auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		Auffuehrung a1 = new Auffuehrung();
		a1.setDatumuhrzeit(new Date());
		a1.setPreis(PreisKategorie.MINDESTPREIS);

		auffuehrungDao.persist(a1);

		Auffuehrung a2 = new Auffuehrung();
		a2.setDatumuhrzeit(new Date());
		a2.setPreis(PreisKategorie.STANDARDPREIS);

		auffuehrungDao.persist(a2);

		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		TransaktionService service = new TransaktionServiceImpl();

		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t1 = new Transaktion();
		t1.setDatumuhrzeit(new Date());
		t1.setStatus(Transaktionsstatus.BUCHUNG);
		t1.setZahlungsart(Zahlungsart.VORKASSE);
		t1.setKunde(k);
		t1.setPlaetze(plaetze);
		transaktionDao.persist(t1);

		plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a2);
		}

		Transaktion t2 = new Transaktion();
		t2.setDatumuhrzeit(new Date());
		t2.setStatus(Transaktionsstatus.BUCHUNG);
		t2.setZahlungsart(Zahlungsart.VORKASSE);
		t2.setKunde(k);
		t2.setPlaetze(plaetze);
		transaktionDao.persist(t2);

		assertEquals(2, transaktionDao.findAll().size());

		service.cancelTransaktion(k, a1);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k.equals(trans.getKunde())) {

				if (trans.getPlaetze().iterator().hasNext() && a1.equals(trans.getPlaetze().iterator().next().getAuffuehrung())) {
					assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

					for (Platz p : trans.getPlaetze()) {
						assertEquals(PlatzStatus.FREI, p.getStatus());
					}
				} else {
					assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

					for (Platz p : trans.getPlaetze()) {
						assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
					}
				}

			}
		}

		service.cancelTransaktion(k, a2);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k.equals(trans.getKunde())) {
				assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.FREI, p.getStatus());
				}
			}
		}
	}

	@Test
	public void testCancelTransaktion_shouldOnlyCancelTranaktionWithAssociatedKunde() {
		KundeDao kundenDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		Kunde k1 = EntityGenerator.getValidKunde(0);
		kundenDao.persist(k1);

		Kunde k2 = EntityGenerator.getValidKunde(1);
		kundenDao.persist(k2);

		AuffuehrungDao auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		Auffuehrung a1 = new Auffuehrung();
		a1.setDatumuhrzeit(new Date());
		a1.setPreis(PreisKategorie.MINDESTPREIS);

		auffuehrungDao.persist(a1);

		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		TransaktionService service = new TransaktionServiceImpl();

		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t1 = new Transaktion();
		t1.setDatumuhrzeit(new Date());
		t1.setStatus(Transaktionsstatus.BUCHUNG);
		t1.setZahlungsart(Zahlungsart.VORKASSE);
		t1.setKunde(k1);
		t1.setPlaetze(plaetze);
		transaktionDao.persist(t1);

		plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		Transaktion t2 = new Transaktion();
		t2.setDatumuhrzeit(new Date());
		t2.setStatus(Transaktionsstatus.BUCHUNG);
		t2.setZahlungsart(Zahlungsart.VORKASSE);
		t2.setKunde(k2);
		t2.setPlaetze(plaetze);
		transaktionDao.persist(t2);

		assertEquals(2, transaktionDao.findAll().size());

		service.cancelTransaktion(k1, a1);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k1.equals(trans.getKunde())) {

				if (trans.getPlaetze().iterator().hasNext() && a1.equals(trans.getPlaetze().iterator().next().getAuffuehrung())) {
					assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

					for (Platz p : trans.getPlaetze()) {
						assertEquals(PlatzStatus.FREI, p.getStatus());
					}
				} else {
					assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

					for (Platz p : trans.getPlaetze()) {
						assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
					}
				}
			} else {
				assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
				}
			}
		}

		service.cancelTransaktion(k2, a1);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k2.equals(trans.getKunde())) {
				assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.FREI, p.getStatus());
				}
			} else {
				assertEquals(Transaktionsstatus.STORNO, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.FREI, p.getStatus());
				}
			}
		}
	}

	@Test
	public void testCancelTransaktion_shouldNotCancelAnyTransaktionWithUnassociatedAuffuehrung() {
		KundeDao kundenDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		Kunde k = EntityGenerator.getValidKunde(0);
		kundenDao.persist(k);

		AuffuehrungDao auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		Auffuehrung a1 = new Auffuehrung();
		a1.setDatumuhrzeit(new Date());
		a1.setPreis(PreisKategorie.MINDESTPREIS);

		auffuehrungDao.persist(a1);

		Auffuehrung a2 = new Auffuehrung();
		a2.setDatumuhrzeit(new Date());
		a2.setPreis(PreisKategorie.STANDARDPREIS);

		auffuehrungDao.persist(a2);

		Auffuehrung a3 = new Auffuehrung();
		a2.setDatumuhrzeit(new Date());
		a2.setPreis(PreisKategorie.STANDARDPREIS);

		auffuehrungDao.persist(a2);

		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		TransaktionService service = new TransaktionServiceImpl();

		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t1 = new Transaktion();
		t1.setDatumuhrzeit(new Date());
		t1.setStatus(Transaktionsstatus.BUCHUNG);
		t1.setZahlungsart(Zahlungsart.VORKASSE);
		t1.setKunde(k);
		t1.setPlaetze(plaetze);
		transaktionDao.persist(t1);

		plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a2);
		}

		Transaktion t2 = new Transaktion();
		t2.setDatumuhrzeit(new Date());
		t2.setStatus(Transaktionsstatus.BUCHUNG);
		t2.setZahlungsart(Zahlungsart.VORKASSE);
		t2.setKunde(k);
		t2.setPlaetze(plaetze);
		transaktionDao.persist(t2);

		assertEquals(2, transaktionDao.findAll().size());

		service.cancelTransaktion(k, a3);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k.equals(trans.getKunde())) {

				assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
				}

			}
		}

		service.cancelTransaktion(k, a3);

		for (Transaktion trans : transaktionDao.findAll()) {
			if (k.equals(trans.getKunde())) {

				assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

				for (Platz p : trans.getPlaetze()) {
					assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
				}

			}
		}
	}

	@Test
	public void testCancelTransaktion_shouldNotCancelAnyTranaktionWithUnassociatedKunde() {
		KundeDao kundenDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
		Kunde k1 = EntityGenerator.getValidKunde(0);
		kundenDao.persist(k1);

		Kunde k2 = EntityGenerator.getValidKunde(1);
		kundenDao.persist(k2);

		Kunde k3 = EntityGenerator.getValidKunde(2);
		kundenDao.persist(k3);

		AuffuehrungDao auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
		Auffuehrung a1 = new Auffuehrung();
		a1.setDatumuhrzeit(new Date());
		a1.setPreis(PreisKategorie.MINDESTPREIS);

		auffuehrungDao.persist(a1);

		LinkedHashSet<Platz> plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		TransaktionDao transaktionDao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
		TransaktionService service = new TransaktionServiceImpl();

		assertEquals(0, transaktionDao.findAll().size());

		Transaktion t1 = new Transaktion();
		t1.setDatumuhrzeit(new Date());
		t1.setStatus(Transaktionsstatus.BUCHUNG);
		t1.setZahlungsart(Zahlungsart.VORKASSE);
		t1.setKunde(k1);
		t1.setPlaetze(plaetze);
		transaktionDao.persist(t1);

		plaetze = new LinkedHashSet<Platz>();

		for(int i = 0; i < 10; i++) {
			Platz p = new Platz();
			p.setNummer(i);
			p.setStatus(PlatzStatus.GEBUCHT);
			plaetze.add(p);
			p.setAuffuehrung(a1);
		}

		Transaktion t2 = new Transaktion();
		t2.setDatumuhrzeit(new Date());
		t2.setStatus(Transaktionsstatus.BUCHUNG);
		t2.setZahlungsart(Zahlungsart.VORKASSE);
		t2.setKunde(k2);
		t2.setPlaetze(plaetze);
		transaktionDao.persist(t2);

		assertEquals(2, transaktionDao.findAll().size());

		service.cancelTransaktion(k3, a1);

		for (Transaktion trans : transaktionDao.findAll()) {
			assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

			for (Platz p : trans.getPlaetze()) {
				assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
			}
		}

		service.cancelTransaktion(k3, a1);

		for (Transaktion trans : transaktionDao.findAll()) {
			assertEquals(Transaktionsstatus.BUCHUNG, trans.getStatus());

			for (Platz p : trans.getPlaetze()) {
				assertEquals(PlatzStatus.GEBUCHT, p.getStatus());
			}
		}
	}
}
