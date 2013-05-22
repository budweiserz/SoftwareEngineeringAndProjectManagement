package at.ticketline.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import at.ticketline.entity.*;

/**
 * Erleichtertes Testen mit dem EntityGenerator:
 * 
 * Aufruf der Methode getValid<Entity>(int modifier) liefert eine initialisierte
 * Instanz der Entitaet, die zum Testen verwendet werden kann
 * Fremdbeziehungen zu anderen Entitaeten sind nicht gesetzt
 * Leicht unterschiedliche Instanzen lassen sich durch unterschiedliche Wahl des
 * Arguments erzielen
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
        k.setEngagements(new HashSet<Engagement>());

        return k;
    }

    public static Kunde getValidKunde(int modifier) {
        
        Kunde k = new Kunde();
        k.setUsername("Username " + modifier);
        k.setPasswort("Passwort " + modifier);
        k.setNachname("Nachname " + modifier);
        k.setVorname("Vorname " + modifier);
        k.setTitel("Titel " + modifier);
        k.setGeschlecht(modifier%2 == 0 ? Geschlecht.WEIBLICH : Geschlecht.MAENNLICH);
        k.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
        k.setTelnr("Telefonnummer " + modifier);
        k.setEmail("email" + modifier + "@foo.com");
        k.setBlz("1234" + modifier % 10);
        k.setKontonr("123" + modifier % 100000);
        k.setKreditkartennr("12345678912" + modifier % 1000);
        k.setKreditkarteGueltigBis(new Date());
        k.setKontostand(new BigDecimal(modifier * 100));
        k.setKontolimit(new BigDecimal(modifier * 1000));
        k.setErmaessigung(new BigDecimal(modifier / 10));
        k.setTicketcardnr("Ticketcardnummer " + modifier);
        k.setTicketcardGueltigBis(new Date());
        k.setVorlieben("Vorlieben " + modifier);
         
        return k;
    }

    public static Mitarbeiter getValidMitarbeiter(int modifier) {
        
        Mitarbeiter m = new Mitarbeiter();
        m.setUsername("Username " + modifier);
        m.setPasswort("Passwort " + modifier);
        m.setNachname("Nachname " + modifier);
        m.setVorname("Vorname " + modifier);
        m.setTitel("Titel " + modifier);
        m.setGeschlecht(Geschlecht.WEIBLICH);
        m.setGeburtsdatum(new GregorianCalendar(1990, 1, 1, 1, 1, 1));
        m.setTelnr("Telefonnummer " + modifier);
        m.setEmail("email" + modifier + "@foo.com");
        m.setBlz("1234" + modifier % 5);
        m.setKontonr("123" + modifier % 100000);
        m.setBerechtigung(Berechtigung.MARKETING);
        m.setSozialversicherungsnr("Sozialv.nr " + modifier % 10);
        
         
        return m;
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
        p.setStatus(PlatzStatus.FREI);

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
