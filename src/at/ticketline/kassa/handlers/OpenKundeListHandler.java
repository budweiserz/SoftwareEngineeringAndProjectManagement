package at.ticketline.kassa.handlers;

public class OpenKundeListHandler extends NewTabHandler {
    public OpenKundeListHandler() {
        super("at.ticketline.partdescriptor.listkunde", "Kundenliste", "Kundenliste anzeigen");
    }
    
    @Override
    protected boolean canOpenMultiple() {
        return false;
    }
}
