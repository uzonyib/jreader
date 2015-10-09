package jreader.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExtendedResponseEntity extends ResponseEntity {
    
    private Object auxiliaryPayload;
    
    public ExtendedResponseEntity(final Object payload, final Object auxiliaryPayload) {
        super(payload);
        this.auxiliaryPayload = auxiliaryPayload;
    }
    
    public Object getAuxiliaryPayload() {
        return auxiliaryPayload;
    }

}
