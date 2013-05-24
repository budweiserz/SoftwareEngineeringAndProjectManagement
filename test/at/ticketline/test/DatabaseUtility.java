package at.ticketline.test;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import at.ticketline.dao.EntityManagerUtil;

public class DatabaseUtility {

    public static void resetDatabase() {

        EntityManager entityManager = EntityManagerUtil.getCurrentEntityManager();

        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        String query = "DELETE FROM ";

        EntityManagerUtil.beginTransaction();

        for (EntityType<?> e : entities) {

            Query q = entityManager.createQuery(query + e.getName());
            q.executeUpdate();
        }

        EntityManagerUtil.commitTransaction();
    }

    /*
     * public static void dropDatabaseSchema() {
     * 
     * EntityManager entityManager = EntityManagerUtil
     * .getCurrentEntityManager();
     * 
     * Query q = entityManager.createNativeQuery("DROP SCHEMA PUBLIC CASCADE");
     * 
     * EntityManagerUtil.beginTransaction(); q.executeUpdate();
     * EntityManagerUtil.commitTransaction(); }
     */
}
