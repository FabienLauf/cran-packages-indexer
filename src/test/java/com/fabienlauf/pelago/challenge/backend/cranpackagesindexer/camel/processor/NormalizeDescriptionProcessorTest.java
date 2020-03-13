package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.TestUtils;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class NormalizeDescriptionProcessorTest {

    @Test
    public void test() throws IOException {
        //Given
        final String rawDescription = TestUtils.getRawDescription();
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(rawDescription)
                .build();
        final NormalizeDescriptionProcessor normalizeDescriptionProcessor = new NormalizeDescriptionProcessor();

        //When
        normalizeDescriptionProcessor.process(exchange);
        String normalizedStr = exchange.getOut().getBody(String.class);

        final String expectedNormalizedStr = TestUtils.getNormalizedDescription();

        //Then
        assertThat(normalizedStr).isEqualTo(expectedNormalizedStr);
    }
}
