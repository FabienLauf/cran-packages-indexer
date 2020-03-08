package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CranPackageRepository extends MongoRepository<CranPackage, String> {

    @Query("{ 'name' : { '$regex' : ?0 , $options: 'i'}}")
    List<CranPackage> findByNameRegexIgnoreCase(final String name);
}
