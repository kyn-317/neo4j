package com.kyn.neo4j.common.dto;

import java.util.Set;

import com.kyn.neo4j.category.Category;
import com.kyn.neo4j.product.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGraph {
    
    private Set<Category> categories;
    private Set<Product> products;
}
