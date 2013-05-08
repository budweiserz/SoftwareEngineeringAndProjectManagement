package at.ticketline.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Geschlecht;
import at.ticketline.entity.Kuenstler;
import at.ticketline.test.AbstractDaoTest;

/**
 * Demonstriert die korrekte Verwendung von AbstractDaoTest
 * 
 * @author Rafael Konlechner
 */
public class DemoTest extends AbstractDaoTest {

    private KuenstlerDao kuenstlerDao;

    @Test
    public void persistValidKuenstler_shouldWork() {
        kuenstlerDao = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);

        /*
         * creating valid entity
         */
        Kuenstler k = new Kuenstler();
        k.setNachname("Peterson");
        k.setVorname("Hansa");
        k.setTitel("DDr.");
        k.setGeschlecht(Geschlecht.WEIBLICH);
        k.setGeburtsdatum(new GregorianCalendar(1970, 11, 2, 13, 20, 0));
        k.setBiographie("Ihr Leben begann bereits kurz nach ihrer Geburt.");

        List<String> violations = new ArrayList<String>();

        try {
            kuenstlerDao.persist(k);
        } catch (ConstraintViolationException cve) {

            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                violations.add(cv.getPropertyPath().toString());
            }
        }
        assertTrue(violations.size() == 0);
    }
}