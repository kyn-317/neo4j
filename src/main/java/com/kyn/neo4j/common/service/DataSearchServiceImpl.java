package com.kyn.neo4j.common.service;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.category.repository.CategoryRepository;
import com.kyn.neo4j.common.dto.ProductGraph;
import com.kyn.neo4j.common.service.interfaces.DataSearchService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DataSearchServiceImpl implements DataSearchService {

    private final CategoryRepository categoryRepository;

    public DataSearchServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Mono<ProductGraph> searchCategory(String categoryString) {
        return categoryRepository.findProductsByCategoryString(categoryString);
    }
    

    

}
