package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.event;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.CranPackagesListRouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.management.event.CamelContextStartedEvent;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.stereotype.Component;

import java.util.EventObject;

@Component
public class CamelContextStartedEventNotifier extends EventNotifierSupport {

    @Override
    public void notify(EventObject event) throws Exception {
        if (event instanceof CamelContextStartedEvent) {
            CamelContext context = ((CamelContextStartedEvent) event).getContext();
            ProducerTemplate t = context.createProducerTemplate();
            t.sendBody(CranPackagesListRouteBuilder.GET_PACKAGES_URL_LIST_ROUTE_URI, null);
        }
    }

    @Override
    public boolean isEnabled(EventObject event) {
        return (event instanceof CamelContextStartedEvent);
    }
}
