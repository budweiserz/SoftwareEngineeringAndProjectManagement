package at.ticketline.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -8138544283872805363L;

    public ServiceException() {
        super();
    }

    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public ServiceException(String msg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(msg, cause, enableSuppression, writableStackTrace);
    }

}
