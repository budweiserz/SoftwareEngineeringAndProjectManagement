package at.ticketline.kassa.ui;

import at.ticketline.entity.Kunde;
import at.ticketline.entity.Zahlungsart;

public class MerchandiseWizardValues {
    private Kunde kunde;
    private Zahlungsart zahlungsart;
    
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
}
