package at.ticketline.service;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.entity.Kunde;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.KundeService;
import at.ticketline.service.impl.KundeServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

/**
 * Testet die Funktionalität der Kunden-Serviceschicht (Einfügen, Bearbeiten)
 * 
 * @author Rafael Konlechner
 */
public class KundeTest extends AbstractDaoTest {

    private static KundeDao kundeDao;
    private static KundeService kundeService;
    private static ArrayList<String> violations;

    @BeforeClass
    public static void init() {

        kundeDao = (KundeDao) DaoFactory.getByEntity(Kunde.class);
        kundeService = new KundeServiceImpl(kundeDao);
        violations = new ArrayList<String>();
    }

    @Before
    public void setup() {

        violations.clear();
    }

    @Test
    public void persistNewValidKunden_shouldWork() {

        for (int i = 0; i <= 100; i++) {
            Kunde k = EntityGenerator.getValidKunde(i);

            try {
                kundeService.save(k);

            } catch (ConstraintViolationException e) {

                for (ConstraintViolation<?> cv : e.getConstraintViolations()) {
                    violations.add(cv.getPropertyPath().toString());
                    System.out.println(cv.getPropertyPath());
                }
            }
            assertTrue(violations.size() == 0);
        }
    }

    @Test(expected = ServiceException.class)
    public void persistNull_shouldThrowException() {

        kundeService.save(null);
    }

    @Test
    public void persistInvalidKunde_shouldThrowException() {

        Kunde k = new Kunde();

        try {
            kundeService.save(k);

        } catch (ConstraintViolationException cve) {

            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                violations.add(cv.getPropertyPath().toString());
                System.out.println("Violation: " + cv.getPropertyPath());
            }
        }
        assertTrue(violations.size() == 3);
    }

    @Test
    public void persistEditedKunde_shouldWork() {

        Kunde k = EntityGenerator.getValidKunde(1);
        try {
            kundeService.save(k);
            k.setUsername("New Username");
            kundeService.save(k);

        } catch (ConstraintViolationException cve) {

            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                violations.add(cv.getPropertyPath().toString());
            }
        }
        assertTrue(violations.size() == 0);
    }
    
    @Test
    public void listAllKunden() {
        kundeService.save(EntityGenerator.getValidKunde(1));
        kundeService.save(EntityGenerator.getValidKunde(2));
        kundeService.save(EntityGenerator.getValidKunde(3));
        assertTrue(kundeService.findAll().size() == 3);
    }
    
    @Test
    public void listAllKundenIs0() {
        assertTrue(kundeService.findAll().size() == 0);
    }
}