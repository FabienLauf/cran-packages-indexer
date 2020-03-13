package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@TestConfiguration
public class PopulatorConfiguration {

    @Value("classpath:data/cranPackages_test_data.json")
    private Resource testData;

    @Bean
    public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
        Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
        factory.setResources(new Resource[] {testData});
        return factory;
    }
}
