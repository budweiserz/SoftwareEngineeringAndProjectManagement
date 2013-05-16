import org.eclipse.persistence.jpa.PersistenceProvider;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.KategorieDao;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
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

		OrtDao daoOrt = (OrtDao)DaoFactory.getByEntity(Ort.class);

		for(int i=0; i<10; i++) {
			Ort o = EntityGenerator.getValidOrt(i);
			o.setAdresse(EntityGenerator.getValidAdresse(i));
			daoOrt.persist(o);
		}

		VeranstaltungDao daoVeranstaltung = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);

		for(int i=0; i<10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			daoVeranstaltung.persist(v);
		}
		
		KategorieDao daoKategorie = (KategorieDao)DaoFactory.getByEntity(Kategorie.class);

		for(int i=0; i<10; i++) {
			Kategorie k = EntityGenerator.getValidKategorie(i);
			daoKategorie.persist(k);
		}
		
		KundeDao daoKunde = (KundeDao) DaoFactory.getByEntity(Kunde.class);
		
		for (int i=0; i<10; i++) {
			Kunde k = EntityGenerator.getValidKunde(i);
			daoKunde.persist(k);
		}
		
		KuenstlerDao daoKuenstler = (KuenstlerDao) DaoFactory.getByEntity(Kuenstler.class);
		
		for(int i=0; i<10; i++) {
			Kuenstler k = EntityGenerator.getValidKuenstler(i);
			daoKuenstler.persist(k);
		}
	}
}
