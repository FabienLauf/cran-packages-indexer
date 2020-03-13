package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component
public class PackageListingToUrlProcessor implements Processor {

    private static final String FILE_NAME = "%s_%s.tar.gz";
    public static final String PACKAGE_TAR_GZ_URL = "https://cran.r-project.org/src/contrib/";

    @Override
    public void process(Exchange exchange) {
        exchange.setPattern(ExchangePattern.InOut);
        exchange.getOut().setHeaders(exchange.getIn().getHeaders());

        final String packageListing = exchange.getIn().getBody(String.class);

        final String[] pkgLstArr = packageListing.split("\n");
        final String packageName = pkgLstArr[0].split(": ")[1];
        final String version = pkgLstArr[1].split(": ")[1];

        final String fileName = String.format(FILE_NAME, packageName, version);

        exchange.getOut().setHeader(Exchange.FILE_NAME, fileName);
        exchange.getOut().setBody(PACKAGE_TAR_GZ_URL + fileName);
    }
}
