package at.ticketline.kassa.handlers;

public class OpenTicketView extends NewTabHandler {
    public OpenTicketView() {
        super("at.ticketline.partdescriptor.TicketView", "TicketAnsicht", "Ansicht für alle gebuchten und reservierten Tickets");
    }
    
    @Override
    protected boolean canOpenMultiple() {
        return false;
    }
}