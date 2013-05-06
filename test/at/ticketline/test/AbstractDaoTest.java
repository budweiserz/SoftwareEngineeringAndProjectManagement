package at.ticketline.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import at.ticketline.dao.EntityManagerUtil;

/**
 * This class serves as a basic structure for dao-tests and will make
 * sure that no permanent changes in the db are made during tests.
 * Every Dao-Test should extend this class
 * @author Rafael Konlechner
 *
 */
public abstract class AbstractDaoTest {
    
    @BeforeClass
    public static void init() {
        TestInitializer.init();
    }

    @Before
    public void setup() {
        EntityManagerUtil.beginTransaction();
    }
    
    @After
    public void teardown() {
        EntityManagerUtil.rollbackTransaction();
    }
}