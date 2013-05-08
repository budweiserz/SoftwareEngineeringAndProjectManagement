package at.ticketline.kassa;

import org.eclipse.persistence.jpa.PersistenceProvider;
import org.jfree.util.Log;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.EntityManagerUtil;
import at.ticketline.dao.api.KuenstlerDao;
import at.ticketline.entity.Kuenstler;
import at.ticketline.service.api.KuenstlerService;
import at.ticketline.service.impl.KuenstlerServiceImpl;

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
		KuenstlerDao kuenstlerDao = (KuenstlerDao)DaoFactory.getByEntity(Kuenstler.class);
		CONTEXT.registerService(KuenstlerService.class.getName(), new KuenstlerServiceImpl(kuenstlerDao), null);
	}

}
