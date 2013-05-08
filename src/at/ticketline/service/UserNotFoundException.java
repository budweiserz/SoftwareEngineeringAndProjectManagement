package at.ticketline.service;

public class UserNotFoundException extends ServiceException {
	private static final long serialVersionUID = -8138544283162805363L;
	
	public UserNotFoundException() {
		super();
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}
}
