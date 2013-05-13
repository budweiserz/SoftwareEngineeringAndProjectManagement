package at.ticketline.test;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import at.ticketline.dao.EntityManagerUtil;

/**
 * Dient als Basisstruktur fuer DAO-Tests und stellt sicher, dass keine
 * permanenten Aenderungen an der Datenbank vorgenommen werden. Vor den Tests
 * wird ein Reset der Datenbank durchgefuehrt, also alle Daten, die durch
 * eventuelle UI-Tests in der Datenbank gespeichert wurden, werden geloescht.
 * Jeder Dao-Test soll von diese Klasse abgeleitet werden
 * 
 * @author Rafael Konlechner
 * 
 */
public abstract class AbstractDaoTest {

    @BeforeClass
    public static void initTest() {
        TestInitializer.init();
        resetDatabase();
    }

    @Before
    public void beginTransaction() {
        EntityManagerUtil.beginTransaction();
    }

    @After
    public void rollbackTransaction() {
        EntityManagerUtil.rollbackTransaction();
    }

    private static void resetDatabase() {

        EntityManager entityManager = EntityManagerUtil
                .getCurrentEntityManager();

        Set<EntityType<?>> entities = entityManager.getMetamodel()
                .getEntities();

        String query = "DELETE FROM ";

        EntityManagerUtil.beginTransaction();

        for (EntityType<?> e : entities) {

            Query q = entityManager.createQuery(query + e.getName());
            q.executeUpdate();
        }

        EntityManagerUtil.commitTransaction();
    }
}