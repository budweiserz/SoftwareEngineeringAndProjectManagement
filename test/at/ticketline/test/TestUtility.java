package at.ticketline.test;

import java.util.ArrayList;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;

import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.entity.Kuenstler;

public class TestUtility {

    public static void resetDatabase() {

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
    
    public static void generateTestData() {
    	
    	resetDatabase();
    	
    	ArrayList<Kuenstler> kuenstler = new ArrayList<Kuenstler>();
    	
    	for (int i = 0; i < 20; ) {
    		
    		
    	}
    }
}
