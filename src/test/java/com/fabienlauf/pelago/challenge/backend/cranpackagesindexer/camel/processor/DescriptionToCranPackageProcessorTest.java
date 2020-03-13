package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.camel.processor;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.Contact;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class DescriptionToCranPackageProcessorTest {

    @Test
    public void test() throws IOException {
        //Given
        final File resource = new ClassPathResource("descriptions/normalized.txt").getFile();
        final Exchange exchange = ExchangeBuilder.anExchange(new DefaultCamelContext())
                .withBody(Files.readAllBytes(resource.toPath()))
                .build();
        final DescriptionToCranPackageProcessor descriptionToCranPackageProcessor = new DescriptionToCranPackageProcessor();

        //When
        descriptionToCranPackageProcessor.process(exchange);
        final CranPackage cranPackage = exchange.getOut().getBody(CranPackage.class);

        final List<Contact> expectedAuthors = Arrays.asList(new Contact("Katalin Csillery"), new Contact("Michael Blum"), new Contact("Olivier Francois", "lerch@mathematik.uni-marburg.gouv"));
        final List<Contact> expectedMaintainers = Arrays.asList(new Contact("Michael Blum", "michael.blum@imag.fr"));
        final CranPackage expectedCranPackage = new CranPackage(
                "abc",
                "1.6",
                new Date(112, 1, 16),
                "Tools for Approximate Bayesian Computation (ABC)",
                "The package implements several ABC algorithms for performing parameter estimation and model selection. Cross-validation tools are also available for measuring the accuracy of ABC estimates, and to calculate the misclassification probabilities of different models.",
                expectedAuthors,
                expectedMaintainers
        );

        //Then
        assertThat(cranPackage).isNotNull();
        assertThat(cranPackage).isEqualToIgnoringGivenFields(expectedCranPackage, "authors", "maintainers");
        assertThat(cranPackage.getAuthors()).isNotNull().isNotEmpty()
                .usingFieldByFieldElementComparator().containsAll(expectedAuthors);
        assertThat(cranPackage.getMaintainers()).isNotNull().isNotEmpty()
                .usingFieldByFieldElementComparator().containsAll(expectedMaintainers);
    }
}
