package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Auffuehrung;

/**
 * Stellt Funktionen, die Auffuehrungen betreffen, bereit
 * @author Michael Ion
 */
public interface AuffuehrungService {
    /**
     * Findet eine Auffuehrung, die den Werten von auffuehrung entspricht
     * @param auffuehrung eine Auffuehrung mit Werten, nach denen gesucht werden soll
     * @param to das Datum bis wohin gesucht werden soll
     * @return eine Liste von Suchergebnissen
     */
    public List<Auffuehrung> find(Auffuehrung auffuehrung, Auffuehrung to);
    
    /**
     * Speichert eine Auffuehrung ab
     * @param auffuehrung die zu speichernde Auffeuhrung
     */
    public void save(Auffuehrung auffuehrung);

}
