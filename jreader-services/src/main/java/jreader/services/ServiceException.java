package jreader.services;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final ServiceStatus status;

    public ServiceException(final String message, final ServiceStatus status) {
        super(message);
        this.status = status;
    }

    public ServiceStatus getStatus() {
        return status;
    }

}
