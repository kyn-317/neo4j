package com.kyn.neo4j.common.service.interfaces;


import com.kyn.neo4j.common.dto.ProductGraph;

import reactor.core.publisher.Mono;

public interface DataSearchService {
    public Mono<ProductGraph> searchCategory(String categoryString);

}
