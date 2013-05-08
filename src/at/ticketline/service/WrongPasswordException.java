package at.ticketline.service;

public class WrongPasswordException extends ServiceException {
	private static final long serialVersionUID = -8138544283162805363L;
	
	public WrongPasswordException() {
		super();
	}

	public WrongPasswordException(String msg) {
		super(msg);
	}
}
