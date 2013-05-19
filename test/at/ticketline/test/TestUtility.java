package at.ticketline.test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import at.ticketline.dao.api.KundeDao;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kunde;

/**
 * 
 * @author Rafael Konlechner
 * 
 * TODO to be continued...
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

    public static void generateTestData() throws FileNotFoundException, IOException {

        LabeledCSVParser parser;
        String[] current;
        Random random = new Random(1210347);

        Adresse adresse;
        Kunde kunde;
        //Kuenstler kuenstler;

        ArrayList<Adresse> adressen = new ArrayList<Adresse>();
        ArrayList<Kunde> kunden = new ArrayList<Kunde>();
        //ArrayList<Kuenstler> kuenstlers = new ArrayList<Kuenstler>();

        KundeDao kundedao = (KundeDao) DaoFactory.getByEntity(Kunde.class);
        //KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory
        //        .getByEntity(Kuenstler.class);
        //EngagementDao engagementDao = (EngagementDao) DaoFactory
        //        .getByEntity(Engagement.class);

        /*
         * reset is done by executing a delete statement on all tables
         */
        resetDatabase();

        // Adresse
        parser = new LabeledCSVParser(new CSVParser(new FileInputStream(
                "csv/adressen.csv")));
        current = parser.getLine();

        while (current != null) {

            adresse = new Adresse();
            adresse.setStrasse(current[0] + " " + current[1]); // strasse +
                                                               // nummer
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
        int i = 0;
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
            System.out.println(kunde.getVorname());
            System.out.println(kunde.getNachname());
            kundedao.persist(kunde);
            current = parser.getLine();
        }

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