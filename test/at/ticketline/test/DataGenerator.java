package at.ticketline.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.ArtikelDao;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.EngagementDao;
import at.ticketline.dao.api.KategorieDao;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.ArtikelKategorie;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.News;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Ortstyp;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Veranstaltung;

import com.Ostermiller.util.CSVParser;
import com.Ostermiller.util.LabeledCSVParser;

public class DataGenerator {

    public static void main(String[] args) {

        TestInitializer.init();

        // TestUtility.dropDatabaseSchema();

        try {
            generateTestData();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void generateTestData() throws FileNotFoundException, IOException {

        LabeledCSVParser parser;
        String[] current;
        Random random = new Random(1210347);

        Adresse adresse;
        Artikel artikel;
        Auffuehrung auffuehrung;
        Kategorie kategorie;
        Kunde kunde;
        Kuenstler kuenstler;
        Mitarbeiter mitarbeiter;
        News news;
        Ort ort;
        Saal saal;
        Veranstaltung veranstaltung;

        ArrayList<Adresse> adressen = new ArrayList<Adresse>();
        ArrayList<Artikel> artikelList = new ArrayList<Artikel>();
        ArrayList<Auffuehrung> auffuehrungList = new ArrayList<Auffuehrung>();
        ArrayList<Kategorie> kategorieList = new ArrayList<Kategorie>();
        ArrayList<Kunde> kunden = new ArrayList<Kunde>();
        ArrayList<Kuenstler> kuenstlerList = new ArrayList<Kuenstler>();
        
        Set<Engagement> musik = new HashSet<Engagement>();
        Set<Engagement> schauspielKino = new HashSet<Engagement>();
        Set<Engagement> schauspielTheater = new HashSet<Engagement>();
        Set<Engagement> schauspielOper = new HashSet<Engagement>();
        Set<Engagement> schauspielKabarett = new HashSet<Engagement>();

        ArrayList<Mitarbeiter> mitarbeiterList = new ArrayList<Mitarbeiter>();
        ArrayList<News> newsList = new ArrayList<News>();
        ArrayList<Ort> ortList = new ArrayList<Ort>();
        ArrayList<Saal> saalList = new ArrayList<Saal>();
        ArrayList<Saal> kinoSaal = new ArrayList<Saal>();
        ArrayList<Saal> theaterSaal = new ArrayList<Saal>();
        ArrayList<Saal> operSaal = new ArrayList<Saal>();
        ArrayList<Saal> kabarettSaal = new ArrayList<Saal>();
        ArrayList<Saal> saalSaal = new ArrayList<Saal>();
        ArrayList<Saal> locationSaal = new ArrayList<Saal>();

        ArrayList<Veranstaltung> veranstaltungList = new ArrayList<Veranstaltung>();

        KundeDao kundedao = (KundeDao) DaoFactory.getByEntity(Kunde.class);

        AuffuehrungDao auffuehrungDao = (AuffuehrungDao) DaoFactory.getByEntity(Auffuehrung.class);
        ArtikelDao artikelDao = (ArtikelDao) DaoFactory.getByEntity(Artikel.class);
        KategorieDao kategorieDao = (KategorieDao) DaoFactory.getByEntity(Kategorie.class);
        KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);
        MitarbeiterDao mitarbeiterDao = (MitarbeiterDao) DaoFactory.getByEntity(Mitarbeiter.class);
        NewsDao newsDao = (NewsDao) DaoFactory.getByEntity(News.class);
        OrtDao ortDao = (OrtDao) DaoFactory.getByEntity(Ort.class);
        SaalDao saalDao = (SaalDao) DaoFactory.getByEntity(Saal.class);
        VeranstaltungDao veranstaltungDao = (VeranstaltungDao) DaoFactory.getByEntity(Veranstaltung.class);
        EngagementDao engagementDao = (EngagementDao) DaoFactory.getByEntity(Engagement.class);

        /*
         * reset is done by executing a delete statement on all tables
         */
        DatabaseUtility.resetDatabase();

        // Mitarbeiter
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/mitarbeiter.csv")));
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
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/news.csv")));
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

        // Adresse (no persist)
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/adressen.csv")));
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
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/kunden.csv")));
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

            kunde.setGeburtsdatum(TestUtility.getRandomGeburtsdatum());
            kunde.setAdresse(adressen.get(random.nextInt(adressen.size())));
            kunden.add(kunde);
            kundedao.persist(kunde);
            current = parser.getLine();
        }
        parser.close();

        // Artikel
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/artikel.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            artikel = EntityGenerator.getValidArtikel(i++);
            artikel.setKurzbezeichnung(current[0]);
            artikel.setBeschreibung(current[1]);
            artikel.setPreis(new BigDecimal(current[2]));

            if (current[3].equals("T-Shirt")) {

                artikel.setKategorie(ArtikelKategorie.TShirt);

            } else if (current[3].equals("CD")) {

                artikel.setKategorie(ArtikelKategorie.CD);

            } else if (current[3].equals("DVD")) {

                artikel.setKategorie(ArtikelKategorie.DVD);

            } else { // (current[3].equals("Sonstiges"))

                artikel.setKategorie(ArtikelKategorie.Sonstiges);
            }

            artikelList.add(artikel);
            artikelDao.persist(artikel);
            current = parser.getLine();
        }
        parser.close();

        // Kuenstler + Engagements
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/kuenstler.csv")));
        current = parser.getLine();
        i = 0;
        Engagement e;
        int jobs;
        while (current != null) {

            kuenstler = EntityGenerator.getValidKuenstler(i++);
            kuenstler.setVorname(current[0]);
            kuenstler.setNachname(current[1]);

            jobs = random.nextInt(5);
            
            if (current[2].equals("m")) {
                kuenstler.setGeschlecht(Geschlecht.MAENNLICH);
            } else {
                kuenstler.setGeschlecht(Geschlecht.WEIBLICH);
            }

            if (current[3].equals("m")) {

                for (int a = 0; a < jobs; a++) {
                    
                    e = EntityGenerator.getValidEngagement(i++);
                    e.setKuenstler(kuenstler);
                    musik.add(e);
                }

            } else if (current[3].equals("t")) {

                for (int a = 0; a < jobs; a++) {
                    
                    e = EntityGenerator.getValidEngagement(i++);
                    e.setKuenstler(kuenstler);
                    schauspielTheater.add(e);
                }

            } else if (current[3].equals("o")) {

                for (int a = 0; a < jobs; a++) {
                    
                    e = EntityGenerator.getValidEngagement(i++);
                    e.setKuenstler(kuenstler);
                    schauspielOper.add(e);
                }

            } else if (current[3].equals("k")) {

                for (int a = 0; a < jobs; a++) {
                    
                    e = EntityGenerator.getValidEngagement(i++);
                    e.setKuenstler(kuenstler);
                    schauspielKabarett.add(e);
                }

            } else { // kino

                for (int a = 0; a < jobs; a++) {
                    
                    e = EntityGenerator.getValidEngagement(i++);
                    e.setKuenstler(kuenstler);
                    schauspielKino.add(e);
                }
            }

            kuenstler.setBiographie(current[4]);
            kuenstlerList.add(kuenstler);
            kuenstlerDao.persist(kuenstler);
            current = parser.getLine();
        }
        parser.close();

        for (int e = 0; e < 1000; e++) {
            auffuehrungList.add(TestUtility.getRandomFutureAuffuehrung(i));
        }

        // Kategorie
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/kategorien.csv")));
        current = parser.getLine();
        i = 0;
        while (current != null) {

            kategorie = EntityGenerator.getValidKategorie(i++);
            kategorie.setBezeichnung(current[0]);
            kategorie.setPreismin(new BigDecimal(current[1]));
            kategorie.setPreismax(new BigDecimal(current[2]));

            kategorieList.add(kategorie);
            kategorieDao.persist(kategorie);
            current = parser.getLine();
        }
        parser.close();

        // Ort + Saal + Reihen
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/saele.csv")));
        current = parser.getLine();
        i = 0;
        String loc;
        Set<Saal> saele = new HashSet<Saal>();

        EntityManagerUtil.beginTransaction();

        while (current != null) {

            ort = EntityGenerator.getValidOrt(i++);
            ort.setBezeichnung(current[1]);

            if (current[2].equals("Kino")) {

                ort.setOrtstyp(Ortstyp.KINO);

            } else if (current[2].equals("Theater")) {

                ort.setOrtstyp(Ortstyp.THEATER);

            } else if (current[2].equals("Oper")) {

                ort.setOrtstyp(Ortstyp.OPER);

            } else if (current[2].equals("Kabarett")) {

                ort.setOrtstyp(Ortstyp.KABARETT);

            } else if (current[2].equals("Saal")) {

                ort.setOrtstyp(Ortstyp.SAAL);

            } else { // if (current[2].equals("location"))

                ort.setOrtstyp(Ortstyp.LOCATION);
            }

            ort.setAdresse(adressen.get(random.nextInt(adressen.size())));

            ortList.add(ort);
            ortDao.persist(ort);

            loc = current[1];

            while (current != null && current[1].equals(loc)) {

                saal = EntityGenerator.getValidSaal(i++);
                saal.setBezeichnung(current[0]);
                saal.setReihen(TestUtility.createReihenForSaal(saal, kategorieList, ort.getOrtstyp()));
                saal.setOrt(ort);

                if (current[2].equals("Kino")) {

                    kinoSaal.add(saal);

                } else if (current[2].equals("Theater")) {

                    theaterSaal.add(saal);

                } else if (current[2].equals("Oper")) {

                    operSaal.add(saal);

                } else if (current[2].equals("Kabarett")) {

                    kabarettSaal.add(saal);

                } else if (current[2].equals("Saal")) {

                    saalSaal.add(saal);

                } else { // if (current[2].equals("location"))

                    locationSaal.add(saal);
                }

                saalList.add(saal);
                saele.add(saal);
                saalDao.persist(saal);
                current = parser.getLine();
            }

            ort.setSaele(saele);
            ortDao.persist(ort);
            saele.clear();
        }

        EntityManagerUtil.commitTransaction();

        parser.close();

        // Auffuehrungen
        for (int e = 0; e < 1000; e++) {

            auffuehrung = EntityGenerator.getValidAuffuehrung(e);
            auffuehrung.setDatumuhrzeit(TestUtility.getRandomAuffuehrungDatum());

            auffuehrungList.add(auffuehrung);
            // auffuehrungDao.persist(auffuehrung);
            current = parser.getLine();
        }

        // Veranstaltungen + Auffuehrungen
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream("csv/veranstaltungen.csv")));
        current = parser.getLine();
        i = 0;
        Set<Auffuehrung> set = new HashSet<Auffuehrung>();
        int index;
        int range;
        EntityManagerUtil.beginTransaction();

        while (current != null) {

            veranstaltung = EntityGenerator.getValidVeranstaltung(i++);
            veranstaltung.setBezeichnung(current[0]);
            veranstaltung.setKategorie(current[1]);
            veranstaltung.setBewertung(current[2]);
            veranstaltung.setInhalt(current[3]);

            index = random.nextInt(974);
            range = index + 1 + random.nextInt(24);

            for (; index < range; index++) {

                auffuehrung = auffuehrungList.get(index % auffuehrungList.size());
                auffuehrung.setVeranstaltung(veranstaltung);

                if (veranstaltung.getKategorie().equals("Kino")) {

                    auffuehrung.setSaal(kinoSaal.get(random.nextInt(kinoSaal.size())));

                } else if (veranstaltung.getKategorie().equals("Theater")) {

                    auffuehrung.setSaal(theaterSaal.get(random.nextInt(theaterSaal.size())));

                } else if (veranstaltung.getKategorie().equals("Oper")) {

                    auffuehrung.setSaal(operSaal.get(random.nextInt(operSaal.size())));

                } else if (veranstaltung.getKategorie().equals("Kabarett")) {

                    auffuehrung.setSaal(kabarettSaal.get(random.nextInt(kabarettSaal.size())));

                } else if (random.nextBoolean()) {

                    auffuehrung.setSaal(locationSaal.get(random.nextInt(locationSaal.size())));

                } else {

                    auffuehrung.setSaal(saalSaal.get(random.nextInt(saalSaal.size())));
                }

                auffuehrungDao.persist(auffuehrung);
                set.add(auffuehrung);
            }

            veranstaltung.setAuffuehrungen(set);
            veranstaltungList.add(veranstaltung);
            veranstaltungDao.persist(veranstaltung);
            set.clear();
            current = parser.getLine();
        }

        EntityManagerUtil.commitTransaction();

        parser.close();
    }
}