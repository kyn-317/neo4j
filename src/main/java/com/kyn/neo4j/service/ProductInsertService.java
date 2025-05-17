package com.kyn.neo4j.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.category.Category;
import com.kyn.neo4j.category.CategoryNode;
import com.kyn.neo4j.category.CategoryRepository;
import com.kyn.neo4j.category.MasterNode;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductInsertService {
    private final CategoryRepository categoryRepository;
    private final MasterNode masterNode;

    public ProductInsertService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.masterNode = new MasterNode();
    }

    public MasterNode getMasterNode() {
        return masterNode;
    }
    public Mono<Void> deleteAllProducts() {
        return categoryRepository.deleteAll();
    }

    // Create tree structure in MasterNode
    public Mono<Void> createTreePath(String categoryString) {
        try {
            masterNode.addCategoryPath(categoryString);
            return Mono.empty();
        } catch (Exception e) {
            log.error("Error creating tree path for category: {}", categoryString, e);
            return Mono.error(e);
        }
    }

    // Create category hierarchy in database from MasterNode
    public Mono<Void> createCategoryHierarchy() {
        log.info("Creating category hierarchy from MasterNode");
        log.info("MasterNode structure:\n{}", masterNode.toString());
        
        return Flux.fromIterable(masterNode.getChildren().entrySet())
            .flatMap(entry -> {
                String rootCategory = entry.getKey();
                log.info("Processing root category: {}", rootCategory);
                return createCategoryNode(entry.getValue(), null);
            })
            .then();
    }

    private Mono<Category> createCategoryNode(CategoryNode node, Category parentCategory) {
        if (node == null || node.getName() == null) {
            log.warn("Invalid category node encountered");
            return Mono.empty();
        }

        return getOrCreateCategory(node.getName())
            .flatMap(category -> {
                if (parentCategory != null) {
                    category.setParentCategory(parentCategory);
                    return categoryRepository.save(category);
                }
                return Mono.just(category);
            })
            .flatMap(category -> 
                Flux.fromIterable(node.getChildren().entrySet())
                    .flatMap(childEntry -> createCategoryNode(childEntry.getValue(), category))
                    .then(Mono.just(category))
            );
    }

    private Mono<Category> getOrCreateCategory(String categoryName) {
        return categoryRepository.findByName(categoryName)
            .switchIfEmpty(
                categoryRepository.save(Category.builder()
                    .name(categoryName)
                    .build())
            );
    }
}

