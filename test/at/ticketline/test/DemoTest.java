package at.ticketline.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Kuenstler;

/**
 * Demonstrates the proper usage of AbstractDaoTest and EntityGenerator
 * 
 * @author Rafael Konlechner
 */
public class DemoTest extends AbstractDaoTest {

    private static KuenstlerDao kuenstlerDao;

    /*
     * BeforeClass of superclass will be called first
     */
    @BeforeClass
    public static void setup() {
        
        kuenstlerDao = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);
    }
    
    /**
     * Example
     */
    @Test
    public void persistValidKuenstler_shouldWork() {

        /*
         * create a valid entity with EntityGenerator
         */
        Kuenstler k = EntityGenerator.getValidKuenstler(0);
        
        /*
         * change prefered values
         */
        k.setNachname("Peterson");
        k.setBiographie("Ihr Leben begann bereits kurz nach ihrer Geburt.");

        List<String> violations = new ArrayList<String>();

        try {
            /*
             * db-operations will only happen in the scope of the current test
             */
            kuenstlerDao.persist(k);
        } catch (ConstraintViolationException cve) {

            for (ConstraintViolation<?> cv : cve.getConstraintViolations()) {
                violations.add(cv.getPropertyPath().toString());
            }
        }
        List<Kuenstler> kuenstler = kuenstlerDao.findAll();
        assertTrue(kuenstler.size() == 1);
        assertTrue(violations.size() == 0);
    }
}