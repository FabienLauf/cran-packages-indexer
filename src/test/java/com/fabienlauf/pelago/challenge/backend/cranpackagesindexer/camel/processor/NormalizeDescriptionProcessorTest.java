package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.Utils;
import org.apache.camel.Exchange;
import org.apache.camel.builder.ExchangeBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class NormalizeDescriptionProcessorTest {

    @Test
    public void test() throws IOException {
        //Given
        final File sourceFile = new ClassPathResource("descriptions/raw.txt").getFile();
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(Files.readAllBytes(sourceFile.toPath()))
                .build();
        final NormalizeDescriptionProcessor normalizeDescriptionProcessor = new NormalizeDescriptionProcessor();

        //When
        normalizeDescriptionProcessor.process(exchange);
        String normalizedStr = exchange.getOut().getBody(String.class);
        Map<String, String> normalizedMap = Utils.dcfToMap(normalizedStr);

        final File expectedNormalizedFile = new ClassPathResource("descriptions/normalized.txt").getFile();
        Map<String, String> expectedMap = Utils.dcfToMap(new String(Files.readAllBytes(expectedNormalizedFile.toPath())));

        //Then
        assertThat(normalizedMap).containsExactlyInAnyOrderEntriesOf(expectedMap);
    }
}
