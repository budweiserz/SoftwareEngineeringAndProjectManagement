package at.ticketline.test;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import at.ticketline.dao.EntityManagerUtil;

/**
 * Dient als Basisstruktur fuer DAO-Tests und stellt sicher, dass keine
 * permanenten Aenderungen an der Datenbank vorgenommen werden.
 * Jeder Dao-Test soll von diese Klasse abgeleitet werden
 * 
 * @author Rafael Konlechner
 *
 */
public abstract class AbstractDaoTest {
    
    @BeforeClass
    public static void initTest() {
        TestInitializer.init();
    }

    @Before
    public void beginTransaction() {
        EntityManagerUtil.beginTransaction();
    }
    
    @After
    public void rollbackTransaction() {
        EntityManagerUtil.rollbackTransaction();
    }
}