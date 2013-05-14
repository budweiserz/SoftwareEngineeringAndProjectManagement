package at.ticketline.service.api;

import java.util.List;

import at.ticketline.entity.Kunde;

/**
 * @author Rafael Konlechner
 */
public interface KundeService {

    /**
     * Speichert einen neuen bzw. bestehenden Kunden, wenn die Attributwerte
     * gültig sind (laut Validator).
     * 
     * @param kunde
     *            != null, wird gespeichert
     */
    public void save(Kunde kunde);
    
    public List<Kunde> findAll();
}