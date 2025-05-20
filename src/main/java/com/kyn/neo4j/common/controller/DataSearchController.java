package com.kyn.neo4j.common.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.neo4j.common.dto.ProductGraph;
import com.kyn.neo4j.common.service.interfaces.DataSearchService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/search")
public class DataSearchController {
    
    private final DataSearchService dataSearchService;

    public DataSearchController(DataSearchService dataSearchService) {
        this.dataSearchService = dataSearchService;
    }

    @PostMapping
    public Mono<ProductGraph> searchCategory(@RequestBody String categoryString) {

        log.info("Searching for category: {}", categoryString);
        return dataSearchService.searchCategory(categoryString);
    }

}
