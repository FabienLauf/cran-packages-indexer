package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository.CranPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = CranPackageRepository.class)
public class Application {

	@Value("classpath:data/test_data.json")
	private Resource testData;

	@Autowired
	CranPackageRepository cranPackageRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public Jackson2RepositoryPopulatorFactoryBean repositoryPopulator() {
		Jackson2RepositoryPopulatorFactoryBean factory = new Jackson2RepositoryPopulatorFactoryBean();
		// inject your Jackson Object Mapper if you need to customize it:
		factory.setResources(new Resource[] {testData});
		return factory;
	}
}
