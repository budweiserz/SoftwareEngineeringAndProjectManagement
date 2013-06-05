package at.ticketline.kassa.handlers;

public class OpenVeranstaltungsSucheHandler extends NewTabHandler {
    public OpenVeranstaltungsSucheHandler() {
        super("at.ticketline.partdescriptor.searchVeranstaltung", "Suche Veranstaltung", "Eine Suche für eine Veranstaltung starten");
    }
    
    @Override
    protected boolean canOpenMultiple() {
        return false;
    }
}