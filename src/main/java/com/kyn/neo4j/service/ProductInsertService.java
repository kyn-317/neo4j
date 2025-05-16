package com.kyn.neo4j.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.category.Category;
import com.kyn.neo4j.category.CategoryRepository;
import com.kyn.neo4j.product.Product;
import com.kyn.neo4j.product.ProductData;
import com.kyn.neo4j.product.ProductMapper;
import com.kyn.neo4j.product.ProductRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class ProductInsertService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductInsertService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }


    @Transactional
    public void importProduct(ProductData productData){
    }
    
    public Mono<Void> deleteAllProducts(){
        return productRepository.deleteAll()
        .then(categoryRepository.deleteAll());
    }

    private Mono<Category> getOrCreateCategory(String categoryName){
        return categoryRepository.findByName(categoryName)
        .switchIfEmpty(categoryRepository.save(Category.builder().name(categoryName).build()))
        .flatMap(category -> Mono.just(category));
    }
    public Mono<Product> createProduct(ProductData productData){
        return createCategoryHierarchy(productData.getCategoryString())
        .flatMap(category -> {
            Product product = ProductMapper.mapToProduct(productData, category);
            return productRepository.save(product);
        });
    }

    private Mono<Category> createCategoryHierarchy(String categoryName){
        List<String> categoryNames = Arrays.asList(categoryName.split("\\|"));

        if (categoryNames.isEmpty()) {
            return Mono.empty();
        }

        return Flux.fromIterable(categoryNames)
            .map(String::trim)
            .collectList()
            .flatMap(names -> {
                // start from most specific category
                int lastIndex = names.size() - 1;
                String deepestCategoryName = names.get(lastIndex);
                
                // create the deepest category
                return getOrCreateCategory(deepestCategoryName)
                    .flatMap(deepestCategory -> {
                        // connect the remaining categories one by one
                        Mono<Category> currentCategoryMono = Mono.just(deepestCategory);
                        
                        // traverse from lastIndex-1 to 0
                        for (int i = lastIndex - 1; i >= 0; i--) {
                            final int currentIdx = i;
                            currentCategoryMono = currentCategoryMono.flatMap(childCategory -> 
                                 getOrCreateCategory(names.get(currentIdx))
                                    .flatMap(parentCategory -> {
                                        childCategory.setParentCategory(parentCategory);
                                        return categoryRepository.save(childCategory)
                                            .thenReturn(parentCategory);
                                    })
                            );
                        }
                        
                        // return the deepest category
                        return currentCategoryMono
                            .then(categoryRepository.findByName(deepestCategoryName));
                    });
            });
    }
}

