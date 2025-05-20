package com.kyn.neo4j.common.service.interfaces;

import com.kyn.neo4j.category.MasterNode;
import com.kyn.neo4j.common.dto.ProductWithCategory;
import com.kyn.neo4j.product.ProductData;

import reactor.core.publisher.Mono;

public interface DataInserService {
    //delete all products and categories
    public Mono<Void> deleteAllProducts();
    //create category hierarchy from master node
    public Mono<Void> createCategoryHierarchyFromMasterNode(MasterNode node);
    //insert products
    public Mono<ProductWithCategory> insertProducts(ProductData product);
}
