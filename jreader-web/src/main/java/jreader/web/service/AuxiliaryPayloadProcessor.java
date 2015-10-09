package jreader.web.service;

import jreader.web.controller.util.AuxiliaryPayloadType;

public interface AuxiliaryPayloadProcessor {
    
    Object process(AuxiliaryPayloadType auxiliaryPayload, String username);

}
