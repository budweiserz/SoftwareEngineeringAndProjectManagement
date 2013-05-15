package at.ticketline.kassa;

import org.eclipse.persistence.jpa.PersistenceProvider;
import org.jfree.util.Log;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.MitarbeiterDao;
import at.ticketline.dao.api.NewsDao;
import at.ticketline.dao.api.OrtDao;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.News;
import at.ticketline.entity.Ort;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.AuffuehrungService;
import at.ticketline.service.api.KuenstlerService;
import at.ticketline.service.api.KundeService;
import at.ticketline.service.api.MitarbeiterService;
import at.ticketline.service.api.NewsService;
import at.ticketline.service.api.OrtService;
import at.ticketline.service.api.VeranstaltungService;
import at.ticketline.service.impl.AuffuehrungServiceImpl;
import at.ticketline.service.impl.KuenstlerServiceImpl;
import at.ticketline.service.impl.KundeServiceImpl;
import at.ticketline.service.impl.MitarbeiterServiceImpl;
import at.ticketline.service.impl.NewsServiceImpl;
import at.ticketline.service.impl.OrtServiceImpl;
import at.ticketline.service.impl.VeranstaltungServiceImpl;



public class Activator implements BundleActivator {

    // The plug-in ID
    public static final String PLUGIN_ID = "at.ticketline.kassa";

    private static BundleContext CONTEXT;

    static BundleContext getContext() {
        return CONTEXT;
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        Activator.CONTEXT = bundleContext;

        EntityManagerUtil.init("ticketline", new PersistenceProvider());

        this.registerServices();
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        Log.info("Stopping bundle " + PLUGIN_ID);

        Activator.CONTEXT = null;

        EntityManagerUtil.closeFactory();
    }

    private void registerServices() {
    	AuffuehrungDao auffuehrungDao = (AuffuehrungDao) DaoFactory.getByEntity(Auffuehrung.class);
       	CONTEXT.registerService(AuffuehrungService.class.getName(), new AuffuehrungServiceImpl(auffuehrungDao), null);
        
    	KuenstlerDao kuenstlerDao = (KuenstlerDao)DaoFactory.getByEntity(Kuenstler.class);
        CONTEXT.registerService(KuenstlerService.class.getName(), new KuenstlerServiceImpl(kuenstlerDao), null);
        
       	KundeDao kundeDao = (KundeDao) DaoFactory.getByEntity(Kunde.class);
       	CONTEXT.registerService(KundeService.class.getName(), new KundeServiceImpl(kundeDao), null);
        
       	MitarbeiterDao mitarbeiterDao = (MitarbeiterDao) DaoFactory.getByEntity(Mitarbeiter.class);
        CONTEXT.registerService(MitarbeiterService.class.getName(), new MitarbeiterServiceImpl(mitarbeiterDao), null);
        
        NewsDao newsDao = (NewsDao)DaoFactory.getByEntity(News.class);
        CONTEXT.registerService(NewsService.class.getName(), new NewsServiceImpl(newsDao), null);
        
        OrtDao ortDao = (OrtDao)DaoFactory.getByEntity(Ort.class);
        CONTEXT.registerService(OrtService.class.getName(), new OrtServiceImpl(ortDao), null);

        OrtDao veranstaltungsOrtDao = (OrtDao) DaoFactory.getByEntity(Ort.class);
        CONTEXT.registerService(OrtService.class.getName(), new OrtServiceImpl(veranstaltungsOrtDao), null);
        
        VeranstaltungDao veranstaltungDao = (VeranstaltungDao) DaoFactory.getByEntity(Veranstaltung.class);
       	CONTEXT.registerService(VeranstaltungService.class.getName(), new VeranstaltungServiceImpl(veranstaltungDao), null);
        
        
    }
}
