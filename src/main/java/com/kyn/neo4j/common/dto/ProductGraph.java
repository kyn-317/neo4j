package com.kyn.neo4j.common.dto;

import java.util.Collection;

import com.kyn.neo4j.category.Category;
import com.kyn.neo4j.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductGraph {

    private Collection<Category> categories;  // List of categories

    private Collection<Product> products;     // List of products

    // for debugging
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Categories:\n");
        if (categories != null) {
            categories.forEach(category -> 
                sb.append(String.format("- %s (id: %s)\n", 
                    category.getName(), 
                    category.getId()))
            );
        }
        
        sb.append("\nProducts:\n");
        if (products != null) {
            products.forEach(product -> 
                sb.append(String.format("- %s (id: %s, price: %.2f)\n", 
                    product.getName(), 
                    product.getId(),
                    product.getSellingPrice()))
            );
        }
        return sb.toString();
    }
}
