package jreader.services;


public class ServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ServiceStatus status;

	public ServiceException(String message, ServiceStatus status) {
		super(message);
		this.status = status;
	}
	
	public ServiceStatus getStatus() {
		return status;
	}

}
