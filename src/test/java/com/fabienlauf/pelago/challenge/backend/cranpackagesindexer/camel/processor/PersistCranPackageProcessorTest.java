package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.TestUtils;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.config.PopulatorConfiguration;
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
        final CranPackage expectedCranPackage = TestUtils.getExpectedCranPackage();
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
