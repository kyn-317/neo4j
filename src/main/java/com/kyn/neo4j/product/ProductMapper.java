package com.kyn.neo4j.product;

import com.kyn.neo4j.category.Category;

public class ProductMapper {
    
    public static Product mapToProduct(ProductData productData, Category category){
        Product product = Product.builder()
            .name(productData.getProductName())
            .description(productData.getDescription())
            .sellingPrice(productData.getPrice())
            .productSpecification(productData.getSpecification())
            .image(productData.getImage())
            .mostSpecificCategory(category)
            .build();
        return product;
    }
}
