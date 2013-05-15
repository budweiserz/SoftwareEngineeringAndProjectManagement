package at.ticketline.dao;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testsuite fuer Persistence-Layer
 * 
 * @author Rafael Konlechner
 */
@RunWith(Suite.class)
@SuiteClasses({
    MitarbeiterTest.class,
    KuenstlerTest.class,
    OrtTest.class,
    AuffuehrungTest.class //,
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