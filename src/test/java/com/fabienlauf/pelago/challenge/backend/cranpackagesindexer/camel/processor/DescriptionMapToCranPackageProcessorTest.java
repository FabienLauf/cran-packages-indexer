package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.TestUtils;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DescriptionMapToCranPackageProcessorTest {

    @Test
    public void test() throws IOException {
        //Given
        final Map<String, String> normalizedMap = TestUtils.getNormalizedMap();
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(normalizedMap)
                .build();
        final DescriptionMapToCranPackageProcessor descriptionMapToCranPackageProcessor = new DescriptionMapToCranPackageProcessor();

        //When
        descriptionMapToCranPackageProcessor.process(exchange);
        final CranPackage cranPackage = exchange.getOut().getBody(CranPackage.class);

        final CranPackage expectedCranPackage = TestUtils.getExpectedCranPackage();

        //Then
        assertThat(cranPackage).isNotNull();
        assertThat(cranPackage).isEqualToIgnoringGivenFields(expectedCranPackage, "authors", "maintainers");
        assertThat(cranPackage.getAuthors()).isNotNull().isNotEmpty()
                .usingFieldByFieldElementComparator().containsAll(expectedCranPackage.getAuthors());
        assertThat(cranPackage.getMaintainers()).isNotNull().isNotEmpty()
                .usingFieldByFieldElementComparator().containsAll(expectedCranPackage.getMaintainers());
    }
}
