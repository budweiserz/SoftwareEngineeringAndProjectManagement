package at.ticketline.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import at.ticketline.dao.DaoFactory;
import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.dao.api.KundeDao;
import at.ticketline.dao.api.SaalDao;
import at.ticketline.dao.api.TransaktionDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kuenstler;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Saal;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.api.TransaktionService;
import at.ticketline.service.impl.TransaktionServiceImpl;
import at.ticketline.test.AbstractDaoTest;
import at.ticketline.test.EntityGenerator;

public class TransaktionTest extends AbstractDaoTest {
    private static KundeDao kundeDao;
    private static AuffuehrungDao auffuehrungDao ;
    private static SaalDao saalDao;
    private static TransaktionDao dao;
    private static TransaktionService service;
    
    @BeforeClass
    public static void init() {
        kundeDao = (KundeDao)DaoFactory.getByEntity(Kunde.class);
        auffuehrungDao = (AuffuehrungDao)DaoFactory.getByEntity(Auffuehrung.class);
        saalDao = (SaalDao)DaoFactory.getByEntity(Saal.class);
        
        dao = (TransaktionDao)DaoFactory.getByEntity(Transaktion.class);
        service = new TransaktionServiceImpl(dao);
    }
    
    @Test
    public void placeholder() {}
}
