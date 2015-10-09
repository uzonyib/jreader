package jreader.web.controller;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntity {

    private int code;
    private String message;
    private Object payload;

    public ResponseEntity() {
        this.code = HttpStatus.OK.value();
    }
    
    public ResponseEntity(final Object payload) {
        this();
        this.payload = payload;
    }

    public ResponseEntity(final int code) {
        this.code = code;
    }

    public ResponseEntity(final int code, final String message) {
        this(code);
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
    
    public Object getPayload() {
        return payload;
    }

}
