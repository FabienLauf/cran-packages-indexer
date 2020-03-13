package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.Contact;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {

    public static String getRawDescription() throws IOException {
        return getStringFromFile("descriptions/raw.txt");
    }

    public static String getNormalizedDescription() throws IOException {
        return getStringFromFile("descriptions/normalized.txt");
    }

    public static String getStringFromFile(String fileClasspath) throws IOException {
        File resource = new ClassPathResource(fileClasspath).getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }

    public static Map<String, String> getNormalizedMap() {
        final Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Package", "abc");
        expectedMap.put("Version", "1.6");
        expectedMap.put("Date", "2012-16-02");
        expectedMap.put("Title", "Tools for Approximate Bayesian Computation (ABC)");
        expectedMap.put("Author", "Katalin Csillery, Michael Blum and Olivier Francois <lerch@mathematik.uni6-marburg.mobi>");
        expectedMap.put("Maintainer", "Michael Blum <michael.blum@imag.fr>");
        expectedMap.put("Depends", "R (>= 2.10), MASS, nnet, quantreg, locfit");
        expectedMap.put("Description", "The package implements several ABC algorithms for performing parameter estimation and model selection. Cross-validation tools are also available for measuring the accuracy of ABC estimates, and to calculate the misclassification probabilities of different models.");
        expectedMap.put("Repository", "CRAN");
        expectedMap.put("License", "GPL (>= 3)");
        expectedMap.put("Packaged", "2012-08-14 15:10:43 UTC; mblum");
        expectedMap.put("Date/Publication", "2012-08-14 16:27:09");
        return expectedMap;
    }

    public static CranPackage getExpectedCranPackage() {
        return new CranPackage(
                "abc",
                "1.6",
                new Date(112, 1, 16),
                "Tools for Approximate Bayesian Computation (ABC)",
                "The package implements several ABC algorithms for performing parameter estimation and model selection. Cross-validation tools are also available for measuring the accuracy of ABC estimates, and to calculate the misclassification probabilities of different models.",
                Arrays.asList(new Contact("Katalin Csillery"), new Contact("Michael Blum"), new Contact("Olivier Francois", "lerch@mathematik.uni6-marburg.mobi")),
                Arrays.asList(new Contact("Michael Blum", "michael.blum@imag.fr"))
        );
    }
}
