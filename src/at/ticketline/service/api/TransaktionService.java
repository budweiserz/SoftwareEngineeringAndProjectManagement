package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;
import at.ticketline.entity.Zahlungsart;

/**
 * Service zum reservieren, verkaufen und stornieren.
 * 
 * @author Florian Klampfer
 */
public interface TransaktionService {

    /**
     * Reserviert Plätze zu einer Veranstaltung für einen neuen, bestehenden oder anonymen Kunden.
     * 
     * @param kunde Ein bestehender oder neuer kunde, null wenn anonymer Kunde
     * @param auffuehrung Aufführung für die reserviert werden soll
     * @param plaetze Ein List von Plätzen die reserviert werden soll. 
     *        Die Plätze dürfen noch nicht verkauft oder reserviert sein.
     * @return Eine Transaktion welche die Reservierungsnummer beinhaltet
     */
    public Transaktion reservieren(Kunde kunde, Auffuehrung auffuehrung, List<Platz> plaetze);
    
    /**
     * Verkauft Plätze zu einer Veranstaltung für einen neuen, bestehenden oder anonymen Kunden.
     * 
     * @param kunde Ein bestehender oder neuer kunde, null wenn anonymer Kunde
     * @param auffuehrung Aufführung für die reserviert werden soll
     * @param plaetze Ein List von Plätzen die reserviert werden soll. 
     *        Die Plätze dürfen noch nicht verkauft oder reserviert sein
     * @return Eine Transaktion welche die Zahlungsinformation beinhaltet
     */
    public Transaktion verkaufen(Kunde kunde, Auffuehrung auffuehrung, List<Platz> plaetze, Zahlungsart z);
}
