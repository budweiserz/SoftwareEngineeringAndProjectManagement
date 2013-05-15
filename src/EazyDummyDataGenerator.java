import org.eclipse.persistence.jpa.PersistenceProvider;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.entity.Ort;
import at.ticketline.test.EntityGenerator;

/**
 * In ya face!!111
 * 
 * @author cell303
 */
public class EazyDummyDataGenerator {
    public static void main(String[] argv) {
		EntityManagerUtil.init("ticketline", new PersistenceProvider());
		
		OrtDao dao = (OrtDao)DaoFactory.getByEntity(Ort.class);
		
		for(int i=0; i<10; i++) {
    		Ort o = EntityGenerator.getValidOrt(i);
    		o.setAdresse(EntityGenerator.getValidAdresse(i));
    		dao.persist(o);
		}
    }
}
