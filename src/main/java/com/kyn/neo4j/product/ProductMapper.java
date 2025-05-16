package com.kyn.neo4j.product;

import com.kyn.neo4j.category.Category;
import java.util.UUID;

public class ProductMapper {
    
    public static Product mapToProduct(ProductData data, Category category) {
        return Product.builder()
            .name(data.getProductName())
            .categoryString(data.getCategoryString())
            .description(data.getDescription())
            .productSpecification(data.getSpecification())
            .sellingPrice(data.getPrice())
            .image(data.getImage())
            .mostSpecificCategory(category)
            .build();
    }
}
