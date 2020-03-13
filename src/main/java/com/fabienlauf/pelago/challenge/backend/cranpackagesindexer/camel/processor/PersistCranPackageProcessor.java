package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository.CranPackageRepository;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersistCranPackageProcessor implements Processor {

    private CranPackageRepository cranPackageRepository;

    @Autowired
    public PersistCranPackageProcessor(CranPackageRepository cranPackageRepository) {
        this.cranPackageRepository = cranPackageRepository;
    }

    @Override
    public void process(Exchange exchange) {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

        final CranPackage cranPackage = exchange.getIn().getBody(CranPackage.class);
        final CranPackage saved = cranPackageRepository.save(cranPackage);

        exchange.getOut().setBody(saved);
    }
}
