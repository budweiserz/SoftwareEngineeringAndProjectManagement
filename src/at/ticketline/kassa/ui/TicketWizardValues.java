package at.ticketline.kassa.ui;

import java.util.HashSet;
import java.util.Set;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Mitarbeiter;
import at.ticketline.entity.Platz;
import at.ticketline.entity.Transaktion;

public class TicketWizardValues {
    private boolean isReservierung;
    private Kunde kunde;
    private Auffuehrung auffuehrung;
    private Mitarbeiter mitarbeiter;
    private HashSet<Platz> plaetze;
    private int reservierungsNummer;
    private Transaktion transaktion;
    
    public Kunde getKunde() {
        return kunde;
    }
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }
    public boolean isReservierung() {
        return isReservierung;
    }
    public void setReservierung(boolean isReservierung) {
        this.isReservierung = isReservierung;
    }
    public Auffuehrung getAuffuehrung() {
        return auffuehrung;
    }
    public void setAuffuehrung(Auffuehrung auffuehrung) {
        this.auffuehrung = auffuehrung;
    }
    public Set<Platz> getPlaetze() {
        return plaetze;
    }
    public void setPlaetze(HashSet<Platz> plaetze) {
    	this.plaetze = plaetze;
    }
    public void setMitarbeiter(Mitarbeiter m) {
        this.mitarbeiter = m;
    }
    public Mitarbeiter getMitarbeiter() {
        return this.mitarbeiter;
    }
    public int getReservierungsNummer() {
        return reservierungsNummer;
    }
    public void setReservierungsNummer(int reservierungsNummer) {
        this.reservierungsNummer = reservierungsNummer;
    }
    public Transaktion getTransaktion() {
        return transaktion;
    }
    public void setTransaktion(Transaktion transaktion) {
        this.transaktion = transaktion;
    }
}
