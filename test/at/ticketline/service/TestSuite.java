package at.ticketline.service;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import at.ticketline.service.KundeTest;

/**
 * Testsuite fuer Service-Layer
 * 
 * @author Rafael Konlechner
 */
@RunWith(Suite.class)
@SuiteClasses({
    KundeTest.class,
    NewsTest.class,
    OrtTest.class,
    AuffuehrungTest.class,
    VeranstaltungTest.class,
    TransaktionTest.class //,
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