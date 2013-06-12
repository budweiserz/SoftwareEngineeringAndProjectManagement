package at.ticketline.kassa.handlers;

public class OpenMerchandiseHandler extends NewTabHandler {
	public OpenMerchandiseHandler() {
		super("at.ticketline.partdescriptor.merchandise", "Merchandiseartikel", "Merchandiseartikel kaufen");
	}
	
	@Override
    protected boolean canOpenMultiple() {
	    return false;
	}
}
