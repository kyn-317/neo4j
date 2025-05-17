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

    public Mono<String> createTreePathFromMasterNode(){
        masterNode.traverse();
        return Mono.just(masterNode.toStringNodeAndParent());
    }



    public Mono<Void> createCategoryHierarchyFromMasterNode() {
        log.info("Creating category hierarchy from MasterNode");
        // First create root category with null parent
        return Flux.fromIterable(masterNode.getChildren().entrySet())
                    // Use concatMap to process one category at a time
                    .concatMap(entry -> {
                        CategoryNode rootNode = entry.getValue();
                        return justCreateCategory(rootNode.getName())
                            .flatMap(category -> {
                                return categoryRepository.save(category)
                                    .flatMap(savedCategory -> 
                                        createHiearchy(rootNode, savedCategory)
                                    );
                            });
                    }, 5) // Process one category at a time
                    .then();
    }

    private Mono<Category> createHiearchy(CategoryNode node, Category parentCategory) {

        return Flux.fromIterable(node.getChildren().entrySet())
            // Use concatMap to process one child at a time
            .concatMap(childEntry -> {
                CategoryNode childNode = childEntry.getValue();
                Category childCategory = Category.builder()
                .name(childNode.getName())
                .parentCategory(parentCategory)
                .build();
                return categoryRepository.save(childCategory)
                .flatMap(savedCategory -> 
                    createHiearchy(childNode, savedCategory)
                );

            }, 5) // Process one child at a time
            .then(Mono.just(parentCategory));
    }
    
    private Mono<Category> justCreateCategory(String categoryName){
        return categoryRepository.save(Category.builder()
            .name(categoryName)
            .build())
            .doOnSuccess(cat -> log.info("Created new category: {}", cat.getName()));
    }

}

