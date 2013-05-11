package at.ticketline.dao.api;

import java.util.List;

import at.ticketline.dao.GenericDao;
import at.ticketline.entity.Auffuehrung;


public interface AuffuehrungDao extends GenericDao<Auffuehrung,Integer> {

    /**
     * Gibt eine Liste der Auffuehrungen zurück, die den Werten von query entsprechen
     * Nach den Kriterien Datum/Uhrzeit, Preis, Veranstaltung und Säle muss gefiltert werden
     */
    public List<Auffuehrung> findByAuffuehrung(Auffuehrung query);
}
