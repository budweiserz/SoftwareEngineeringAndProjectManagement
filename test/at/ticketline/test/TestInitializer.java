package at.ticketline.test;

import org.apache.log4j.BasicConfigurator;
import org.eclipse.persistence.jpa.PersistenceProvider;

import at.ticketline.dao.EntityManagerUtil;

/**
 * Initialisiert die Testumgebung.
 * Die init-Methode muss am Anfang jedes Testlaufs aufgerufen werden,
 * um die Testumgebung zu initialisieren.
 * Es wird nicht auf die Produktiv-Datenbank zugegriffen, sondern auf die Test-Datenbank
 */
public class TestInitializer {
	
	private static boolean INITIALIZED = false;

	public static void init() {
		if (INITIALIZED) {
			return;
		}
		
		//Unit Tests are done on a separate db, called the test-db
		EntityManagerUtil.init("test", new PersistenceProvider());
		INITIALIZED = true;
		
		BasicConfigurator.configure();
	}

}
