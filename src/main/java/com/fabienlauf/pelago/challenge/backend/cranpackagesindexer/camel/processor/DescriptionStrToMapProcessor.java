package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DescriptionStrToMapProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

        final String description = exchange.getIn().getBody(String.class);
        final Map<String, String> descriptionMap = Arrays.stream(description.split("\n"))
                .filter(s -> s.matches("^[\\w/]+: .+$"))
                .map(s -> s.split(": ", 2)) //"Description: foo:bar" -> ["Description", "foo:bar"]
                .collect(Collectors.toMap(k -> k[0], v -> v[1]));

        exchange.getOut().setBody(descriptionMap);
    }
}
