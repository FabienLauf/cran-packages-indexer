package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository.CranPackageRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = CranPackageRepository.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
