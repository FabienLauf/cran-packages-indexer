package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.config.PopulatorConfiguration;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(PopulatorConfiguration.class)
@DataMongoTest
public class CranPackageRepositoryTest {

    @Autowired
    private CranPackageRepository cranPackageRepository;

    @Test
    public void whenFindAllByNameRegexIgnoreCase_ThenReturnsAllMatchingCranPackages() {
        final CranPackage shape = new CranPackage("shape");
        final CranPackage seqHMM = new CranPackage("seqHMM");

        List<CranPackage> packages = cranPackageRepository.findAllByNameRegexIgnoreCase("s");

        assertThat(packages).isNotNull();
        assertThat(packages).hasSize(2);
        assertThat(packages)
                .usingElementComparatorOnFields("name")
                .containsExactly(shape, seqHMM);
    }

    @Test
    public void whenSearchingNonMatchingString_ThenReturnsEmptyList() {
        List<CranPackage> packages = cranPackageRepository.findAllByNameRegexIgnoreCase("nope");

        assertThat(packages).isNotNull();
        assertThat(packages).isEmpty();
    }
}
