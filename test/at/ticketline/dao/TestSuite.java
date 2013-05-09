package at.ticketline.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testsuite fuer Service- und Persistence-Layer
 * 
 * @author Rafael Konlechner
 */
@RunWith(Suite.class)
@SuiteClasses({
    KundeTest.class,
    MitarbeiterTest.class,
    KuenstlerTest.class //,
    //add your test here
    })

public class TestSuite {

    @BeforeClass
    public static void setup() {
        
        System.out.println("Starte Test-Suite ...");
    }
    
    @AfterClass
    public static void teardown() {
        
        System.out.println("Test-Suite beendet.");
    }
}