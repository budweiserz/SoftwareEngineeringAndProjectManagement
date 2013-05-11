package at.ticketline.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.api.AuffuehrungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.service.api.AuffuehrungService;

/**
 * @author Michael Ion
 */
public class AuffuehrungServiceImpl implements AuffuehrungService {

    private static final Logger LOG = LoggerFactory.getLogger(AuffuehrungServiceImpl.class);
    private AuffuehrungDao auffuehrungDao;
    
    public AuffuehrungServiceImpl(AuffuehrungDao auffuehrungDao) {
        this.auffuehrungDao = auffuehrungDao;
    }
    
    @Override
    public List<Auffuehrung> find(Auffuehrung auffuehrung) {
        LOG.info("Suche nach Auffuehrung mit den Kriterien: Preis: {}, Datum/Uhrzeit: {}, Veranstaltung: {}, Saele: {}",
                auffuehrung.getPreis().toString(), auffuehrung.getDatumuhrzeit().toString(),
                auffuehrung.getVeranstaltung(), auffuehrung.getSaal()
                );
        return auffuehrungDao.findByAuffuehrung(auffuehrung);
    }

}
