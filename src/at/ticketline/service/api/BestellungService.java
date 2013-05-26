package at.ticketline.service.api;

import java.util.HashMap;
import java.util.List;

import at.ticketline.entity.Artikel;
import at.ticketline.entity.Bestellung;

public interface BestellungService {
	
	/**
	 * Diese Methode speichert ein Bestellung, die aus verschieden Artikeln mit unterschiedlicher Stueckzahl besteht.
	 * Wirft eine @exception IllegalArgumentException sofern betsellungen null ist.
	 * @param bestellungen Eine HashMap die als Schluessel den gewuenschten Artikel und als zugehoerigen Wert die jeweilige Anzahl beinhaltet.
	 */
	public void saveBestellungen(HashMap<Artikel, Integer> bestellungen);
	
	/**
	 * Liefert eine Liste aller Bestellungen die getätigt wurden.
	 * @return eine Liste an Bestellungen
	 */
	public List<Bestellung> findAll();

}
