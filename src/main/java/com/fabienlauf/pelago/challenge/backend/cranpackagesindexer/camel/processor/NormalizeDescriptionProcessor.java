package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class NormalizeDescriptionProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

        final String descriptionStr = exchange.getIn().getBody(String.class);

        /*
         1. Some fields of the Description file can have multiple lines
            Normalizing fields so their content fits in one line.
         2. Author and Maintainer fields can have options like [aut, cre] that should not be part of Name.
         */
        final String normalized = descriptionStr.replaceAll("\n\\s+"," ")
                                                .replaceAll("\\s\\[[a-z, ]+\\]", "");

        exchange.getOut().setBody(normalized);
    }
}
