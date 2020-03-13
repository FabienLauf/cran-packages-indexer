package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.config.PopulatorConfiguration;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.Contact;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository.CranPackageRepository;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(PopulatorConfiguration.class)
@DataMongoTest
public class PersistCranPackageProcessorTest {

    @Autowired
    private CranPackageRepository cranPackageRepository;

    @Test
    public void test() {
        //Given
        final List<Contact> expectedAuthors = Arrays.asList(new Contact("Katalin Csillery"), new Contact("Michael Blum"), new Contact("Olivier Francois"));
        final List<Contact> expectedMaintainers = Arrays.asList(new Contact("Michael Blum", "michael.blum@imag.fr"));
        final CranPackage expectedCranPackage = new CranPackage(
                "abc",
                "1.6",
                new Date(112, 1, 16),
                "Tools for Approximate Bayesian Computation (ABC)",
                "The package implements several ABC algorithms for performing parameter",
                expectedAuthors,
                expectedMaintainers
        );
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(expectedCranPackage)
                .build();
        final PersistCranPackageProcessor persistCranPackageProcessor = new PersistCranPackageProcessor(cranPackageRepository);

        //When
        persistCranPackageProcessor.process(exchange);
        final CranPackage cranPackage = exchange.getOut().getBody(CranPackage.class);

        //Then
        assertThat(cranPackage.getId()).isNotBlank();
    }
}
