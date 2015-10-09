package jreader.web.service.impl;

import jreader.services.SubscriptionService;
import jreader.web.controller.util.AuxiliaryPayloadType;
import jreader.web.service.AuxiliaryPayloadProcessor;

public class AuxiliaryPayloadProcessorImpl implements AuxiliaryPayloadProcessor {
    
    private final SubscriptionService subscriptionService;
    
    public AuxiliaryPayloadProcessorImpl(final SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @Override
    public Object process(final AuxiliaryPayloadType auxiliaryPayload, final String username) {
        Object payload = null;
        if (auxiliaryPayload == AuxiliaryPayloadType.SUBSCRIPTIONS) {
            payload = subscriptionService.list(username);
        }
        return payload;
    }

}
