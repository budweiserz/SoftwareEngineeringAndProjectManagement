package at.ticketline.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Ortstyp;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;

/**
 * Klasse kapselt oft benoetigte Methoden und Routinen beim Generieren von
 * Testdaten
 * 
 * @author Rafael Konlechner
 */
public class TestUtility {

    private static Random random = new Random(1218327824);

    /**
     * liefert ein zufaelliges Geburtsdatum im Bereich 1950-2000
     * 
     * @return Datum zwischen 1950-2000, Monatstage nur jeweils bis zum 27.
     */
    public static GregorianCalendar getRandomGeburtsdatum() {

        int year = 1950 + random.nextInt(50);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);
        int hour = 0;
        int minute = 0;
        int second = 0;
        GregorianCalendar output = new GregorianCalendar(year, month, day, hour, minute, second);

        return output;
    }

    /**
     * liefert ein zufaelliges Datum im Bereich von Mitte Mai 2013
     * 
     * @return Datum zwischen 20. und 30. Mai 2013
     */
    public static GregorianCalendar getRandomNewsDatum() {

        int year = 2013;
        int month = 5;
        int day = 20 + random.nextInt(10);
        int hour = 0;
        int minute = 0;
        int second = 0;
        GregorianCalendar output = new GregorianCalendar(year, month, day, hour, minute, second);

        return output;
    }

    /**
     * liefert ein zufaelliges Datum im Bereich der Monate Juni bis Dezember
     * 2013
     * 
     * @return Datum zwischen Juni und Dezember 2013, Monatstage ausser Feb.
     *         jeweils bis zum 30. Tag
     */
    public static Date getRandomAuffuehrungDatum() {

        int year = 2013;
        int month = 5 + random.nextInt(6);
        int day;
        if (month != 2) {
            day = random.nextInt(30);
        } else {
            day = random.nextInt(28);
        }
        int hour = 12 + random.nextInt(12);
        int minute = 0 + 15 * random.nextInt(3);
        int second = 0;
        String date = day + "." + month + "." + year + " " + hour + ":" + minute + ":" + second;
        Date output = null;

        try {
            output = parseDate(date);

        } catch (ParseException e) {

            e.printStackTrace();
        }

        return output;
    }

    /**
     * liefert eine Auffuehrung, die im Bereich von
     * <code>getRandomAuffuehrungDatum()</code> stattfindet
     */
    public static Auffuehrung getRandomFutureAuffuehrung(int i) {

        Auffuehrung output = EntityGenerator.getValidAuffuehrung(i);
        output.setDatumuhrzeit(getRandomAuffuehrungDatum());

        return output;
    }

    /**
     * Liefert Reihen eines Saals, zufaellige Anzahl an Reihen pro Kategorie
     * 
     * @param saal
     *            Saal, dem die Reihe zugeordnet ist
     * @param kategorieList
     *            Preiskategorie der Reihe
     * @param typ
     *            Ortstyp des Saals
     * @return Reihen eines Saals, zufaellige Anzahl an Reihen pro Kategorie
     */
    public static Set<Reihe> createReihenForSaal(Saal saal, ArrayList<Kategorie> kategorieList, Ortstyp typ) {

        HashSet<Reihe> set = new HashSet<Reihe>();

        boolean choose;

        int order = 0;
        int seats = 1;
        int tmp;

        for (Kategorie k : kategorieList) {

            tmp = (int) (getSeats(typ) / 2) + random.nextInt(getSeats(typ) / 2);
            choose = random.nextBoolean();

            if (choose) {

                for (int i = 0; i < random.nextInt(getRows(typ)); i++) {

                    set.add(createReihe(tmp, order, seats, saal, k, typ));
                    seats += tmp;
                    order++;
                }
            }
        }

        return set;
    }

    /**
     * Liefert eine Reihe eines Saals
     * 
     * @param seats
     *            Anzahl der Sitze
     * @param order
     *            Reihenfolge der Reihe (Reihe A vor Reihe B)
     * @param start
     *            Startnummer des ersten Sitztes
     * @param saal
     *            Zugeordneter Saal
     * @param k
     *            Zugeordnete Preiskategorie
     * @param typ
     *            Ortstyp des Saals
     * @return Reihe eines Saals
     */
    private static Reihe createReihe(int seats, int order, int start, Saal saal, Kategorie k, Ortstyp typ) {

        Reihe r = EntityGenerator.getValidReihe(order);
        r.setBezeichnung("Reihe " + order);
        r.setSaal(saal);
        r.setReihenfolge(order);
        r.setAnzplaetze(seats);
        r.setStartplatz(start);
        r.setKategorie(k);

        if (typ.equals(Ortstyp.LOCATION)) {

            r.setSitzplatz(false);
        }

        return r;
    }

    /**
     * Parst Datum aus String mit <code>SimpleDateFormat</code>
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

        Date output = format.parse(date);

        return output;
    }

    /**
     * Regelt die maximal zulaessige Anzahl an Reihen pro Kategorie fuer einen
     * speziellen Ortstyp Beispiel: Ein Kinosaal soll nicht so viele Reihen
     * haben wie eine Open Air Arena
     * 
     * @param t
     *            entsprechender Ortstyp
     * @return maximale Anzahl an Reihen pro Kategorie
     */
    private static int getRows(Ortstyp t) {

        if (t.equals(Ortstyp.KINO)) {

            return 5;

        } else if (t.equals(Ortstyp.THEATER)) {

            return 20;

        } else if (t.equals(Ortstyp.OPER)) {

            return 20;

        } else if (t.equals(Ortstyp.KABARETT)) {

            return 10;

        } else if (t.equals(Ortstyp.SAAL)) {

            return 50;

        } else if (t.equals(Ortstyp.LOCATION)) {

            return 100;

        } else {

            return 0;
        }
    }

    /**
     * Regelt die maximal zulaessige Anzahl an Sitzen pro Reihe fuer einen
     * speziellen Ortstyp Beispiel: Ein Kinosaal soll nicht so viele Sitze pro
     * Reihe haben wie eine Open Air Arena
     * 
     * @param t
     *            entsprechender Ortstyp
     * @return maximale Anzahl an Sitzen pro Reihe
     */
    private static int getSeats(Ortstyp t) {

        if (t.equals(Ortstyp.KINO)) {

            return 20;

        } else if (t.equals(Ortstyp.THEATER)) {

            return 30;

        } else if (t.equals(Ortstyp.OPER)) {

            return 30;

        } else if (t.equals(Ortstyp.KABARETT)) {

            return 20;

        } else if (t.equals(Ortstyp.SAAL)) {

            return 50;

        } else if (t.equals(Ortstyp.LOCATION)) {

            return 200;

        } else {

            return 0;
        }
    }
}