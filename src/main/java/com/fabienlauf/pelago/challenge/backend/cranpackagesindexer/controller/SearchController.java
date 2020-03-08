package com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.controller;

import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.model.CranPackage;
import com.fabienlauf.pelago.challenge.backend.cranpackagesindexer.repository.CranPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private CranPackageRepository cranPackageRepository;

    @GetMapping("")
    public List<CranPackage> byName(@RequestParam String q) {
        return cranPackageRepository.findByNameRegexIgnoreCase(q);
    }

    @GetMapping("/all")
    public List<CranPackage> all() {
        return cranPackageRepository.findAll();
    }
}
