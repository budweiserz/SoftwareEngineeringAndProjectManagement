package at.ticketline.kassa.ui;

import at.ticketline.entity.Auffuehrung;
import at.ticketline.entity.Kunde;

public class TransaktionWizardValues {
    private boolean isReservierung;
    private Kunde kunde;
    private Auffuehrung auffuehrung;
    
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
}
