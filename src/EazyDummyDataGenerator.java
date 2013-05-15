import org.eclipse.persistence.jpa.PersistenceProvider;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.KategorieDao;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Veranstaltung;
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

		VeranstaltungDao daoV = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);

		for(int i=0; i<10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			daoV.persist(v);
		}
		
		KategorieDao daoK = (KategorieDao)DaoFactory.getByEntity(Kategorie.class);

		for(int i=0; i<10; i++) {
			Kategorie k = EntityGenerator.getValidKategorie(i);
			daoK.persist(k);
		}
	}
}
