package jreader.services;

public enum ServiceStatus {

    OK(200),
    INVALID_INPUT(400),
    RESOURCE_NOT_FOUND(404),
    RESOURCE_ALREADY_EXISTS(409),
    OTHER_ERROR(500);

    private int code;

    private ServiceStatus(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
