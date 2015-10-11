package jreader.web.service.impl;

import jreader.services.SubscriptionGroupService;
import jreader.web.controller.util.AuxiliaryPayloadType;
import jreader.web.service.AuxiliaryPayloadProcessor;

public class AuxiliaryPayloadProcessorImpl implements AuxiliaryPayloadProcessor {
    
    private final SubscriptionGroupService subscriptionGroupService;
    
    public AuxiliaryPayloadProcessorImpl(final SubscriptionGroupService subscriptionGroupService) {
        this.subscriptionGroupService = subscriptionGroupService;
    }

    @Override
    public Object process(final AuxiliaryPayloadType auxiliaryPayload, final String username) {
        Object payload = null;
        if (auxiliaryPayload == AuxiliaryPayloadType.SUBSCRIPTIONS) {
            payload = subscriptionGroupService.list(username);
        }
        return payload;
    }

}
