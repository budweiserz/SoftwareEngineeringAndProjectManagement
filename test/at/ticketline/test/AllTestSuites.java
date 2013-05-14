package at.ticketline.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testsuite aller Unittests des Projekts
 * 
 * @author Rafael Konlechner
 */
@RunWith(Suite.class)
@SuiteClasses({
    at.ticketline.dao.TestSuite.class,
    at.ticketline.service.TestSuite.class //,
    //add your test here
    })

public class AllTestSuites {

    @BeforeClass
    public static void setup() {
        
        System.out.println("Starte Test-Suite ...");
    }
    
    @AfterClass
    public static void teardown() {
        
        System.out.println("Test-Suite beendet.");
    }
}