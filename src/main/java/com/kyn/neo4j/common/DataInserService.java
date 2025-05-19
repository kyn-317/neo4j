package com.kyn.neo4j.common;

import com.kyn.neo4j.product.ProductData;

import reactor.core.publisher.Mono;

public interface DataInserService {
    //delete all products and categories
    public Mono<Void> deleteAllProducts();
    //create category tree
    public Mono<Void> createTreePath(String categoryString);
    //create category hierarchy from master node
    public Mono<Void> createCategoryHierarchyFromMasterNode();
    //insert products
    public Mono<ProductWithCategory> insertProducts(ProductData product);
}
