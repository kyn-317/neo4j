package com.kyn.neo4j.common.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.category.entity.Category;
import com.kyn.neo4j.category.repository.CategoryRepository;
import com.kyn.neo4j.category.util.CategoryNode;
import com.kyn.neo4j.category.util.MasterNode;
import com.kyn.neo4j.common.dto.ProductWithCategory;
import com.kyn.neo4j.common.service.interfaces.DataInserService;
import com.kyn.neo4j.product.dto.ProductData;
import com.kyn.neo4j.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DataInsertServiceImpl implements DataInserService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataInsertServiceImpl(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    //delete all products and categories
    @Override
    public Mono<Void> deleteAllProducts() {
        return categoryRepository.deleteAll()
        .then(productRepository.deleteAll());
    }

    //create category hierarchy from master node
    @Override
    public Mono<Void> createCategoryHierarchyFromMasterNode(MasterNode node) {
        log.info("Creating category hierarchy from MasterNode");
        // First create root category with null parent
        return Flux.fromIterable(node.getChildren().entrySet())
                    // Use concatMap to process 3 category at a time
                    .concatMap(entry -> {
                        CategoryNode rootNode = entry.getValue();
                        return justCreateCategory(rootNode.getName())
                            .flatMap(category -> 
                                 categoryRepository.save(category)
                                    .flatMap(savedCategory -> createHiearchy(rootNode, savedCategory))
                            );
                        }, 3).then();
    }

    //insert products
    @Override
    public Mono<ProductWithCategory> insertProducts(ProductData product){
        return categoryRepository.saveProductWithExactCategory(
            UUID.randomUUID(),
            product.getProductName(),
            product.getDescription(),
            product.getPrice(),
            product.getSpecification(),
            product.getImage(),
            product.getCategoryString()
        );
    }

    //create category hierarchy from master node
    private Mono<Category> createHiearchy(CategoryNode node, Category parentCategory) {

        return Flux.fromIterable(node.getChildren().entrySet())
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
            }, 3) 
            .then(Mono.just(parentCategory));
    }

    //create category
    private Mono<Category> justCreateCategory(String categoryName){
        return categoryRepository.save(Category.builder()
            .name(categoryName)
            .build())
            .doOnSuccess(cat -> log.info("Created new category: {}", cat.getName()));
    }

 
       

}

