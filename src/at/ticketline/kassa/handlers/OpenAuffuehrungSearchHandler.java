package at.ticketline.kassa.handlers;

public class OpenAuffuehrungSearchHandler extends NewTabHandler {
    public OpenAuffuehrungSearchHandler() {
        super("at.ticketline.partdescriptor.searchAuffuehrung", "Suche Aufführungen", "Eine Suche für eine Aufführung starten");
    }
    
    @Override
    protected boolean canOpenMultiple() {
        return false;
    }
}