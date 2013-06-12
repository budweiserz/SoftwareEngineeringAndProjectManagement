package at.ticketline.kassa.handlers;

public class OpenTopTenVeranstaltungenHandler extends NewTabHandler {
    public OpenTopTenVeranstaltungenHandler() {
        super("at.ticketline.partdescriptor.topTenVeranstaltungen", "Top 10 Veranstaltungen", "Top 10 Veranstaltungen ansehen");
    }

	@Override
    protected boolean canOpenMultiple() {
	    return false;
	}
}