package at.ticketline.test;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.EngagementDao;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Kuenstler;

public class TestUtility {

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

    public static void generateTestData() {

        resetDatabase();

        KuenstlerDao kuenstlerDao = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);
        EngagementDao engagementDao = (EngagementDao) DaoFactory.getByEntity(Engagement.class);

        Kuenstler k;
        Engagement e;

        ArrayList<Kuenstler> kuenstler = new ArrayList<Kuenstler>();
        ArrayList<Engagement> engagements = new ArrayList<Engagement>();

        // Kuenstler
        for (int i = 0; i < 20; i++) {

            k = EntityGenerator.getValidKuenstler(i);
            kuenstler.add(k);
            kuenstlerDao.persist(k);
        }

        // Engagements
        for (int i = 0; i < 200; i++) {

            e = EntityGenerator.getValidEngagement(i);
            engagements.add(e);
            engagementDao.persist(e);

            kuenstler.get(i % 20).getEngagements().add(e);
            e.setKuenstler(kuenstler.get(i % 20));
        }

        /*
         * setting foreign relationships
         */
        EntityManagerUtil.beginTransaction();

        for (int i = 0; i < 200; i++) {

            engagementDao.persist(engagements.get(i));
            if (i < 20) {
                kuenstlerDao.persist(kuenstler.get(i));
            }
        }

        EntityManagerUtil.commitTransaction();
    }
}