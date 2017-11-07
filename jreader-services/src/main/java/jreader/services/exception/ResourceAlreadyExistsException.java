package jreader.services.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ResourceAlreadyExistsException() {
        super();
    }

    public ResourceAlreadyExistsException(final String message) {
        super(message);
    }

}
