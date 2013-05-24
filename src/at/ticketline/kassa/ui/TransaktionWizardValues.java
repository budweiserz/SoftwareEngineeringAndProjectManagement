package at.ticketline.kassa.ui;

import at.ticketline.entity.Kunde;

public class TransaktionWizardValues {
    private boolean isReservierung;
    private Kunde kunde;
    
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
}
