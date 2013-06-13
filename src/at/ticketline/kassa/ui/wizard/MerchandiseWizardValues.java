package at.ticketline.kassa.ui.wizard;

import java.util.HashMap;

import at.ticketline.entity.Artikel;
import at.ticketline.entity.Kunde;
import at.ticketline.entity.Zahlungsart;

public class MerchandiseWizardValues {
    private Kunde kunde;
    private Zahlungsart zahlungsart;
    private HashMap<Artikel, Integer> selected;
    
    public Kunde getKunde() {
        return kunde;
    }
    public void setKunde(Kunde kunde) {
        this.kunde = kunde;
    }
    public Zahlungsart getZahlungsart() {
        return zahlungsart;
    }
    public void setZahlungsart(Zahlungsart zahlungsart) {
        this.zahlungsart = zahlungsart;
    }
    public HashMap<Artikel, Integer> getSelected() {
        return selected;
    }
    public void setSelected(HashMap<Artikel, Integer> selected) {
        this.selected = selected;
    }
}
