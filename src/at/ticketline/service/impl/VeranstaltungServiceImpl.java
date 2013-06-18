package at.ticketline.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.ticketline.dao.DaoException;
import at.ticketline.dao.api.VeranstaltungDao;
import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Platz;
import at.ticketline.entity.PlatzStatus;
import at.ticketline.entity.Veranstaltung;
import at.ticketline.service.ServiceException;
import at.ticketline.service.api.VeranstaltungService;

public class VeranstaltungServiceImpl implements VeranstaltungService {
	private static final Logger LOG = LoggerFactory.getLogger(VeranstaltungServiceImpl.class);

	private VeranstaltungDao veranstaltungDao;

	public VeranstaltungServiceImpl(VeranstaltungDao veranstaltungDao) {
		this.veranstaltungDao = veranstaltungDao;
	}

	/**
	 * Sucht nach ähnlichen Veranstaltungen, die mit der übergebenen Veranstaltung übereinstimmen
	 * 
	 * @param veranstaltung Das Veranstaltungsobjekt, dass die Suchkriterien beinhaltet
	 * @param minDauer die minimale Dauer, die die gefundenen Veranstaltungen haben sollen
	 * @param maxDauer die maximale Dauer, die die gefundenen Veranstaltungen haben sollen
	 * @return Eine Liste aller gefundenen Veranstaltungen
	 */
	@Override
	public List<Veranstaltung> find(Veranstaltung veranstaltung, Integer minDauer, Integer maxDauer) {
		return this.veranstaltungDao.findByVeranstaltung(veranstaltung, minDauer, maxDauer);
	}

	/**
	 * Speichert eine neue bzw. eine bereits bestehende Veranstaltung.
	 *  
	 * @param veranstaltung Die Veranstaltung, die gespeichert werden soll
	 */
	@Override
	public void save(Veranstaltung veranstaltung) {
		try {
			if (veranstaltung.getId() == null) {
				LOG.info("Neue Veranstaltung speichern: {} (Kategorie: {}, Inhalt: {})", veranstaltung.getBezeichnung(), veranstaltung.getKategorie(), veranstaltung.getInhalt());
				this.veranstaltungDao.persist(veranstaltung);
			} else {
				LOG.info("Bestehende Veranstaltung speichern: {} (ID: {})", veranstaltung.getBezeichnung(), veranstaltung.getId());
				this.veranstaltungDao.merge(veranstaltung);
			}
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}
	
	/**
	 * Liefert die 10 Veranstaltungen mit den meisten verkauften Tickets.
	 * 
	 * @param start Startdatum, von dem Angefangen werden soll zu suchen
	 * @param end Enddatum, bis dahin wird nach den Top 10 Veranstaltungen gesucht
	 * @param kategorie Die Kategorie der Veranstaltung
	 * @return Eine Liste der Top 10 Veranstaltungen und deren Anzahl an verkauften Tickets
	 */
	@Override
	public LinkedHashMap<Veranstaltung, Integer> findTopTen(Date start, Date end, String kategorie) {
		HashMap<Veranstaltung, Integer> topTenList = new HashMap<Veranstaltung, Integer>();
		LinkedHashMap<Veranstaltung, Integer> sortedTopTenList = new LinkedHashMap<Veranstaltung, Integer>();
		LinkedHashMap<Veranstaltung, Integer> finalTopTenList = new LinkedHashMap<Veranstaltung, Integer>();
		
		// Veranstaltungsobjekt mit der gesuchten Kategorie
		Veranstaltung queryVeranstaltung = new Veranstaltung();
		queryVeranstaltung.setKategorie(kategorie);
		
		// Liste aller existierenden Veranstaltungen
		List<Veranstaltung> allVeranstaltungen = this.veranstaltungDao.findByVeranstaltung(queryVeranstaltung, null, null);
		Set<Auffuehrung> allAuffuehrungen;
		
		// Iteratoren
		Iterator<Veranstaltung> itVeranstaltung = allVeranstaltungen.iterator();
		Iterator<Auffuehrung> itAuffuehrung;
		Iterator<Map.Entry<Veranstaltung, Integer>> itTopTenVeranstaltung;
		
		// Hilfsobjekte fuer die Iteratoren
		Veranstaltung currentVeranstaltung = null;
		Auffuehrung currentAuffuehrung = null;
		Map.Entry<Veranstaltung, Integer> currentTopTenVeranstaltung = null;
		
		int plaetze = 0; // Anzahl der verkauften Tickets/Plaetze pro Veranstaltung
		
		// alle vorhandenen Veranstaltungen iterieren
		while (itVeranstaltung.hasNext()) {
			currentVeranstaltung = itVeranstaltung.next();
			
			allAuffuehrungen = currentVeranstaltung.getAuffuehrungen();
			itAuffuehrung = allAuffuehrungen.iterator();
			
			// alle Auffuehrungen einer Veranstaltung iterieren und Plaetze zusammenzaehlen
			while (itAuffuehrung.hasNext()) {
				currentAuffuehrung = itAuffuehrung.next();
				
				// nur jene Auffuehrungen fuer die Berechnung beruecksichtigen, die auch innerhalb des Zeitintervalls sind
				// und nur jene Plaetze beruecksichtigen, die auch wirklich gebucht wurden (PlatzStatus.GEBUCHT)
				if (currentAuffuehrung.getDatumuhrzeit().after(start) && currentAuffuehrung.getDatumuhrzeit().before(end)) {
					plaetze += this.countBookedSeats(currentAuffuehrung);
				}
			}
			
			// Anzahl der verkauften Plaetze der Veranstaltung zuweisen
			if (plaetze > 0) {
				topTenList.put( currentVeranstaltung, plaetze );
			}
			
			plaetze = 0;
		}
		
		// liste der Veranstaltungen und deren Anzahl an verkauften Tickets 
		sortedTopTenList = this.sortByValues(topTenList);
		
		// finale Top Ten Liste erstellen mit den beliebtesten 10 Veranstaltungen
		int i = 0;
		itTopTenVeranstaltung = sortedTopTenList.entrySet().iterator();
		while (itTopTenVeranstaltung.hasNext() && i < 10) {
			currentTopTenVeranstaltung = itTopTenVeranstaltung.next();
			
			// Veranstaltung zu neuen Top 10 Liste hinzufügen
			finalTopTenList.put(currentTopTenVeranstaltung.getKey(), currentTopTenVeranstaltung.getValue());
			
			i++;
		}
		
		return finalTopTenList;
	}
	
	/**
	 * Liefert die Anzahl der gebuchten Sitze einer Aufführung
	 * 
	 * @param auffuehrung Die Aufführung, von der die Anzahl der gebuchten Sitze berechnet werden soll
	 * @return Anzahl der gebuchten Sitze der übergebenen Aufführung
	 */
	private int countBookedSeats(Auffuehrung auffuehrung) {
		Iterator<Platz> itPlaetze = auffuehrung.getPlaetze().iterator();
		int count = 0;
		
		while (itPlaetze.hasNext()) {
			// wenn Platz gebucht wurde, dann erhöhe die Anzahl der gebuchten Plätze
			if (itPlaetze.next().getStatus().equals(PlatzStatus.GEBUCHT)) {
				count++;
			}
		}
		
		return count;
	}
	
	/*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable
     * throw NullPointerException if Map contains null values
     * It also sort values even if they are duplicates
     * 
     * Copyright: http://javarevisited.blogspot.com/2012/12/how-to-sort-hashmap-java-by-key-and-value.html#ixzz2TrKSGeJV
     */
    private LinkedHashMap<Veranstaltung, Integer> sortByValues(Map<Veranstaltung, Integer> map){
        List<Map.Entry<Veranstaltung, Integer>> entries = new LinkedList<Map.Entry<Veranstaltung, Integer>>(map.entrySet());
      
        Collections.sort(entries, new Comparator<Map.Entry<Veranstaltung, Integer>>() {
            @Override
            public int compare(Entry<Veranstaltung, Integer> o1, Entry<Veranstaltung, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
      
        //LinkedHashMap will keep the keys in the order they are inserted
        //which is currently sorted on natural ordering
        LinkedHashMap<Veranstaltung, Integer> sortedMap = new LinkedHashMap<Veranstaltung, Integer>();
      
        for(Map.Entry<Veranstaltung, Integer> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
      
        return sortedMap;
    }
}
