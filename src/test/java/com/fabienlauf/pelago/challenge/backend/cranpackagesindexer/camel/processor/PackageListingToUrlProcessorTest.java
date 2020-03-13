package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PackageListingToUrlProcessorTest {

    @Test
    public void whenPackageListingProcessed_thenBodyContainsPackageURL() {
        //Given
        final String packageListing = "Package: adehabitatHR\n" +
                "Version: 0.4.2\n" +
                "Depends: R (>= 2.10.0), sp, methods, deldir, ade4, adehabitatMA, adehabitatLT\n" +
                "Suggests: maptools, tkrplot, MASS, rgeos, gpclib\n" +
                "License: GPL (>= 2)";
        final String expectedFileName = "adehabitatHR_0.4.2.tar.gz";
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                                    .withBody(packageListing)
                                    .build();
        final PackageListingToUrlProcessor packageListingToUrlProcessor = new PackageListingToUrlProcessor();

        //When
        packageListingToUrlProcessor.process(exchange);
        String body = exchange.getOut().getBody(String.class);

        //Then
        assertThat(body).isEqualTo(PackageListingToUrlProcessor.PACKAGE_TAR_GZ_URL+expectedFileName);
        assertThat(exchange.getOut().getHeader(Exchange.FILE_NAME)).isEqualTo(expectedFileName);
    }
}
