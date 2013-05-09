package at.ticketline.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Kuenstler;

/**
 * Demonstriert die korrekte Verwendung von AbstractDaoTest und EntityGenerator
 * 
 * @author Rafael Konlechner
 */
public class DemoTest extends AbstractDaoTest {

    private KuenstlerDao kuenstlerDao;

    @Test
    public void persistValidKuenstler_shouldWork() {
        kuenstlerDao = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);

        /*
         * creating valid entity with EntityGenerator
         */
        Kuenstler k = EntityGenerator.getValidKuenstler(0);
        
        /*
         * change prefered values
         */
        k.setNachname("Peterson");
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