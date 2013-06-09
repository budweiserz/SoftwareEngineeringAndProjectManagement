package at.ticketline.test;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.entity.Adresse;
import at.ticketline.entity.Artikel;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Bestellung;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.News;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Reihe;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Veranstaltung;

/**
 * Test stellt die korrekte Funktionalitaet von EntityGenerator sicher
 * 
 * @author Rafael Konlechner
 * 
 */
public class Test_EntityGenerator {

    Validator v = DaoFactory.getValidator();

    @Test
    public void generateAdresse_shouldBeValid() {

        Adresse a;
        Set<ConstraintViolation<Adresse>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidAdresse(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateArtikel_shouldBeValid() {

        Artikel a;
        Set<ConstraintViolation<Artikel>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidMerchandise(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateAuffuehrung_shouldBeValid() {

        Auffuehrung a;
        Set<ConstraintViolation<Auffuehrung>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidAuffuehrung(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateBestellung_shouldBeValid() {

        Bestellung a;
        Set<ConstraintViolation<Bestellung>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidBestellung(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateEngagement_shouldBeValid() {

        Engagement a;
        Set<ConstraintViolation<Engagement>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidEngagement(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateKuenstler_shouldBeValid() {

        Kuenstler a;
        Set<ConstraintViolation<Kuenstler>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidKuenstler(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateKunde_shouldBeValid() {

        Kunde a;
        Set<ConstraintViolation<Kunde>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidKunde(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }
    
    @Test
    public void generateMitarbeiter_shouldBeValid() {

        Mitarbeiter a;
        Set<ConstraintViolation<Mitarbeiter>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidMitarbeiter(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }
    
    @Test
    public void generateKategorie_shouldBeValid() {

        Kategorie a;
        Set<ConstraintViolation<Kategorie>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidKategorie(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateNews_shouldBeValid() {

        News a;
        Set<ConstraintViolation<News>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidNews(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateOrt_shouldBeValid() {

        Ort a;
        Set<ConstraintViolation<Ort>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidOrt(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generatePlatz_shouldBeValid() {

        Platz a;
        Set<ConstraintViolation<Platz>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidPlatz(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateReihe_shouldBeValid() {

        Reihe a;
        Set<ConstraintViolation<Reihe>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidReihe(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateSaal_shouldBeValid() {

        Saal a;
        Set<ConstraintViolation<Saal>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidSaal(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateTransaktion_shouldBeValid() {

        Transaktion a;
        Set<ConstraintViolation<Transaktion>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidTransaktion(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }

    @Test
    public void generateVeranstaltung_shouldBeValid() {

        Veranstaltung a;
        Set<ConstraintViolation<Veranstaltung>> violations;

        for (int i = 0; i <= 100; i++) {

            a = EntityGenerator.getValidVeranstaltung(i);
            violations = v.validate(a);
            assertTrue(violations.size() == 0);
        }
    }
}
