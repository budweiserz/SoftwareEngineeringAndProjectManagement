import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.persistence.jpa.PersistenceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.EngagementDao;
import at.ticketline.dao.api.KategorieDao;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Engagement;
import at.ticketline.entity.Kategorie;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.News;
import at.ticketline.entity.Ort;
import at.ticketline.entity.PreisKategorie;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.test.EntityGenerator;

/**
 * Populates the database with dummy data.
 * 
 * To clear the databease run "drop schema public cascade".
 * 
 * @author cell303
 */
public class EazyDummyDataGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(EazyDummyDataGenerator.class);
    
	public static void main(String[] argv) {
		EntityManagerUtil.init("ticketline", new PersistenceProvider());

		OrtDao daoOrt = (OrtDao)DaoFactory.getByEntity(Ort.class);

		for(int i=0; i<10; i++) {
			Ort o = EntityGenerator.getValidOrt(i);
			o.setAdresse(EntityGenerator.getValidAdresse(i));
			daoOrt.persist(o);
		}

		VeranstaltungDao daoVeranstaltung = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);
		Veranstaltung[] varr = new Veranstaltung[10];
		for(int i=0; i<10; i++) {
			Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			daoVeranstaltung.persist(v);
			varr[i] = v;
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
		
		AuffuehrungDao auffuehrungDao = (AuffuehrungDao) DaoFactory.getByEntity(Auffuehrung.class);
		Set<Auffuehrung> auffuehrungen = new LinkedHashSet<Auffuehrung>();
		
		for(int i=0; i<10; i++) {
		    Auffuehrung a = EntityGenerator.getValidAuffuehrung(i);
		    a.setPreis(PreisKategorie.MINDESTPREIS);
		    auffuehrungen.add(a);
		    a.setVeranstaltung(varr[i]);
		    auffuehrungDao.persist(a);
		}
		
		NewsDao daoNews = (NewsDao) DaoFactory.getByEntity(News.class);
		
		for(int i=0; i<20; i++) {
			News n = EntityGenerator.getValidNews(i);
			daoNews.persist(n);
		}
		KuenstlerDao daoKu = (KuenstlerDao)DaoFactory.getByEntity(Kuenstler.class);
		EngagementDao daoE = (EngagementDao)DaoFactory.getByEntity(Engagement.class);
		VeranstaltungDao daoVer = (VeranstaltungDao)DaoFactory.getByEntity(Veranstaltung.class);

		for(int i=0; i<10; i++) {
			Kuenstler k = EntityGenerator.getValidKuenstler(i);
			daoKu.persist(k);
			
			//Veranstaltung v = EntityGenerator.getValidVeranstaltung(i);
			Veranstaltung v = varr[i];
			v.setAuffuehrungen(auffuehrungen);
			daoVer.merge(v);
			Set<Engagement> engs = new LinkedHashSet<Engagement>();
			
			for(int j=0; j<10; j++) {
			    Engagement e = EntityGenerator.getValidEngagement(10*i+j);
			    e.setKuenstler(k);
			    e.setVeranstaltung(v);
			    daoE.persist(e);
			    
			    engs.add(e);
			}
			
		    v.setEngagements(engs);
			daoVer.persist(v);
		}
		
		LOG.info("EAZY");
	}
}
