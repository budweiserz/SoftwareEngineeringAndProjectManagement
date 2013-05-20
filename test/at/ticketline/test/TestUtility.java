package at.ticketline.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.ArtikelKategorie;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.News;
import at.ticketline.entity.Veranstaltung;

/**
 * 
 * @author Rafael Konlechner
 * 
 *         TODO to be continued...
 *         schon gemacht:
 *         Adressen
 *         Mitarbeiter
 *         News
 *         Kunden
 *         Veranstaltungen
 * 
 */
public class TestUtility {

    private static Random random = new Random(273890);

    public static void resetDatabase() {

        EntityManager entityManager = EntityManagerUtil
                .getCurrentEntityManager();

        Set<EntityType<?>> entities = entityManager.getMetamodel()
                .getEntities();

        String query = "DELETE FROM ";

        EntityManagerUtil.beginTransaction();

        for (EntityType<?> e : entities) {

            Query q = entityManager.createQuery(query + e.getName());
            q.executeUpdate();
        }

        EntityManagerUtil.commitTransaction();
    }

    /*
    public static void dropDatabaseSchema() {

        EntityManager entityManager = EntityManagerUtil
                .getCurrentEntityManager();

        Query q = entityManager.createNativeQuery("DROP SCHEMA PUBLIC CASCADE");

        EntityManagerUtil.beginTransaction();
        q.executeUpdate();
        EntityManagerUtil.commitTransaction();
    }
    */
    
    public static GregorianCalendar getRandomGeburtsdatum() {

        int year = 1950 + random.nextInt(50);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);
        int hour = 0;
        int minute = 0;
        int second = 0;
        GregorianCalendar output = new GregorianCalendar(year, month, day,
                hour, minute, second);

        return output;
    }
    
    public static GregorianCalendar getRandomNewsDatum() {

        int year = 2013;
        int month = 5;
        int day = 10 + random.nextInt(25);
        int hour = 0;
        int minute = 0;
        int second = 0;
        GregorianCalendar output = new GregorianCalendar(year, month, day,
                hour, minute, second);

        return output;
    }

    public static void generateTestData() throws FileNotFoundException,
            IOException {

        LabeledCSVParser parser;
        String[] current;
        Random random = new Random(1210347);

        Adresse adresse;
        Artikel artikel;
        Kunde kunde;
        Mitarbeiter mitarbeiter;
        News news;
        Veranstaltung veranstaltung;
        // Kuenstler kuenstler;

        ArrayList<Adresse> adressen = new ArrayList<Adresse>();
        ArrayList<Artikel> artikelList = new ArrayList<Artikel>();
        ArrayList<Kunde> kunden = new ArrayList<Kunde>();
        ArrayList<Mitarbeiter> mitarbeiterList = new ArrayList<Mitarbeiter>();
        ArrayList<News> newsList = new ArrayList<News>();
        ArrayList<Veranstaltung> veranstaltungen = new ArrayList<Veranstaltung>();
        
        // ArrayList<Kuenstler> kuenstlers = new ArrayList<Kuenstler>();

        KundeDao kundedao = (KundeDao) DaoFactory.getByEntity(Kunde.class);
        ArtikelDao artikelDao = (ArtikelDao) DaoFactory.getByEntity(Artikel.class);
        MitarbeiterDao mitarbeiterDao = (MitarbeiterDao) DaoFactory.getByEntity(Mitarbeiter.class);
        NewsDao newsDao = (NewsDao) DaoFactory.getByEntity(News.class);
        VeranstaltungDao veranstaltungDao = (VeranstaltungDao) DaoFactory.getByEntity(Veranstaltung.class);
        
        // KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory
        // .getByEntity(Kuenstler.class);
        // EngagementDao engagementDao = (EngagementDao) DaoFactory
        // .getByEntity(Engagement.class);

        /*
         * reset is done by executing a delete statement on all tables
         */
        resetDatabase();

        
        // Mitarbeiter
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/mitarbeiter.csv")));
        current = parser.getLine();
        int i = 0;
        while (current != null) {

            mitarbeiter = EntityGenerator.getValidMitarbeiter(i++);
            mitarbeiter.setUsername(current[0]);
            mitarbeiter.setVorname(current[1]);
            mitarbeiter.setNachname(current[2]);
            mitarbeiter.setPasswort(current[3]);

            mitarbeiterList.add(mitarbeiter);
            mitarbeiterDao.persist(mitarbeiter);
            current = parser.getLine();
        }
        parser.close();

        // News
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/news.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            news = EntityGenerator.getValidNews(i++);
            news.setTitel(current[0]);
            news.setText(current[1]);

            newsList.add(news);
            newsDao.persist(news);
            current = parser.getLine();
        }
        parser.close();
        
        // Adresse
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/adressen.csv")));
        current = parser.getLine();

        while (current != null) {

            adresse = new Adresse();
            adresse.setStrasse(current[0] + " " + current[1]);
            adresse.setPlz(current[2]);
            adresse.setOrt(current[3]);
            adresse.setLand(current[4]);

            adressen.add(adresse);
            current = parser.getLine();
        }
        parser.close();

        // Kunde + Adresse
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/kunden.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            kunde = EntityGenerator.getValidKunde(i++);
            kunde.setVorname(current[0]);
            kunde.setNachname(current[1]);

            if (current[2].equals("m")) {
                kunde.setGeschlecht(Geschlecht.MAENNLICH);
            } else {
                kunde.setGeschlecht(Geschlecht.WEIBLICH);
            }

            kunde.setGeburtsdatum(getRandomGeburtsdatum());
            kunde.setAdresse(adressen.get(random.nextInt(adressen.size())));

            kunden.add(kunde);
            kundedao.persist(kunde);
            current = parser.getLine();
        }
        parser.close();
        
        // Artikel
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/artikel.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            artikel = EntityGenerator.getValidArtikel(i++);
            artikel.setKurzbezeichnung(current[0]);
            artikel.setBeschreibung(current[1]);
            artikel.setPreis(new BigDecimal(current[2]));
            
            if (current[3].equals("T-Shirt")) {
                
                artikel.setKategorie(ArtikelKategorie.TShirt);
                
            } else  if (current[3].equals("CD")) {
                
                artikel.setKategorie(ArtikelKategorie.CD); 
                
            } else  if (current[3].equals("DVD")) {
                
                artikel.setKategorie(ArtikelKategorie.DVD); 
                
            } else { // (current[3].equals("Sonstiges"))
                
                artikel.setKategorie(ArtikelKategorie.Sonstiges); 
            }

            artikelList.add(artikel);
            artikelDao.persist(artikel);
            current = parser.getLine();
        }
        parser.close();
        
        // Veranstaltungen
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/veranstaltungen.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            veranstaltung = EntityGenerator.getValidVeranstaltung(i++);
            veranstaltung.setBezeichnung(current[0]);
            veranstaltung.setKategorie(current[1]);
            veranstaltung.setInhalt(current[2]);  

            veranstaltungen.add(veranstaltung);
            System.out.println(veranstaltung.getBezeichnung());
            veranstaltungDao.persist(veranstaltung);
            current = parser.getLine();
        }
        parser.close();
        
        // Engagements
        /*
         * for (int i = 0; i < 200; i++) {
         * 
         * e = EntityGenerator.getValidEngagement(i); engagements.add(e);
         * engagementDao.persist(e);
         * 
         * kuenstler.get(i % 20).getEngagements().add(e);
         * e.setKuenstler(kuenstler.get(i % 20)); }
         */
        /*
         * setting foreign relationships
         */
        /*
         * EntityManagerUtil.beginTransaction();
         * 
         * for (int i = 0; i < 200; i++) {
         * 
         * engagementDao.persist(engagements.get(i)); if (i < 20) {
         * kuenstlerDao.persist(kuenstler.get(i)); } }
         * 
         * EntityManagerUtil.commitTransaction();
         */
    }
}