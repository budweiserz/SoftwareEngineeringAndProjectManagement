package at.ticketline.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;

import at.ticketline.entity.*;

/**
 * Simplify testing with EntityGenerator:
 * 
 * Calling getValid<Entity>(int modifier) will return an already initialized
 * instance, that is ready to be persisted or merged in the db No foreign
 * relationships to other entities are set Variate entities by choosing
 * different indices Create invalid entities by modifiing single values
 * 
 * @author Rafael Konlechner
 * 
 */
public class EntityGenerator {

    public static Adresse getValidAdresse(int modifier) {

        Adresse a = new Adresse();
        a.setStrasse("Strasse " + modifier);
        a.setPlz("1234" + (modifier % 10));
        a.setOrt("Ort " + modifier);
        a.setLand("Land " + modifier);

        return a;
    }

    public static Artikel getValidArtikel(int modifier) {

        Artikel a = new Artikel();
        a.setKurzbezeichnung("Kurzbezeichnung " + modifier);
        a.setBeschreibung("Beschreibung " + modifier);
        a.setPreis(new BigDecimal(modifier));
        a.setKategorie(ArtikelKategorie.CD);

        return a;
    }

    public static Auffuehrung getValidAuffuehrung(int modifier) {

        Auffuehrung a = new Auffuehrung();
        a.setDatumuhrzeit(new Date());
        a.setPreis(PreisKategorie.MAXIMALPREIS);
        a.setHinweis("Hinweis " + modifier);

        return a;
    }

    public static Bestellung getValidBestellung(int modifier) {

        Bestellung b = new Bestellung();
        b.setBestellzeitpunkt(new Date());
        b.setZahlungsart(Zahlungsart.BANKEINZUG);
        b.setAnmerkungen("Anmerkung " + modifier);

        return b;
    }

    public static Engagement getValidEngagement(int modifier) {

        Engagement e = new Engagement();
        e.setFunktion("Funktion " + modifier);
        e.setGage(new BigDecimal(modifier));

        return e;
    }

    public static Kategorie getValidKategorie(int modifier) {

        Kategorie k = new Kategorie();
        k.setBezeichnung("Bezeichnung " + modifier);
        k.setPreismin(new BigDecimal(modifier));
        k.setPreismax(new BigDecimal(modifier + 1));

        return k;
    }

    public static Kuenstler getValidKuenstler(int modifier) {

        Kuenstler k = new Kuenstler();
        k.setNachname("Nachname " + modifier);
        k.setVorname("Vorname " + modifier);
        k.setTitel("Titel " + modifier);
        k.setGeschlecht(Geschlecht.WEIBLICH);
        k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
        k.setBiographie("Biographie " + modifier);

        return k;
    }

    public static News getValidNews(int modifier) {

        News n = new News();
        n.setDatum(new Date());
        n.setTitel("Titel " + modifier);
        n.setText("Text " + modifier);

        return n;
    }

    public static Ort getValidOrt(int modifier) {

        Ort o = new Ort();
        o.setBezeichnung("Bezeichnung " + modifier);
        o.setOrtstyp(Ortstyp.KABARETT);
        o.setTelnr("" + modifier * 10000);
        o.setBesitzer("Besitzer " + modifier);
        o.setOeffnungszeiten("Oeffungszeiten: " + modifier);
        o.setKiosk(true);
        o.setVerkaufsstelle(true);

        return o;
    }

    public static Platz getValidPlatz(int modifier) {

        Platz p = new Platz();
        p.setNummer(new Integer(modifier));

        return p;
    }

    public static Reihe getValidReihe(int modifier) {

        Reihe r = new Reihe();
        r.setReihenfolge(new Integer(modifier));
        r.setBezeichnung("Bezeichnung " + modifier);
        r.setAnzplaetze(new Integer(modifier));

        return r;
    }

    public static Saal getValidSaal(int modifier) {

        Saal s = new Saal();
        s.setBezeichnung("Bezeichnung " + modifier);

        return s;
    }

    public static Transaktion getValidTransaktion(int modifier) {

        Transaktion t = new Transaktion();
        t.setDatumuhrzeit(new Date());
        t.setStatus(Transaktionsstatus.BUCHUNG);
        t.setReservierungsnr(new Integer(modifier));

        return t;
    }

    public static Veranstaltung getValidVeranstaltung(int modifier) {

        Veranstaltung v = new Veranstaltung();
        v.setBezeichnung("Bezeichnung " + modifier);
        v.setKategorie("Kategorie " + modifier);
        v.setSubkategorie("Subkategorie " + modifier);
        v.setJahrerstellung(new Integer(modifier));
        v.setSpracheton("Spracheton " + modifier);
        v.setSpracheut("Spracheut " + modifier);
        v.setDauer(new Integer(modifier));
        v.setFreigabe("Freigabe " + modifier);
        v.setAbbildung("Abbildung " + modifier);
        v.setInhalt("Inhalt " + modifier);
        v.setKritik("Kritik " + modifier);
        v.setBewertung("Bewertung " + modifier);

        return v;
    }
}
