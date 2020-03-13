package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.TestUtils;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

import java.io.IOException;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DescriptionStrToMapProcessorTest {

    @Test
    public void test() throws IOException {
        //Given
        final String normalizedStr = TestUtils.getNormalizedDescription();
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(normalizedStr)
                .build();
        final DescriptionStrToMapProcessor descriptionStrToMapProcessor = new DescriptionStrToMapProcessor();

        //When
        descriptionStrToMapProcessor.process(exchange);
        final Map<String, String> normalizedMap = exchange.getOut().getBody(Map.class);

        final Map<String, String> expectedMap = TestUtils.getNormalizedMap();

        //Then
        assertThat(normalizedMap).containsExactlyInAnyOrderEntriesOf(expectedMap);
    }

}
