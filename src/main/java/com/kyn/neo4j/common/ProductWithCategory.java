package com.kyn.neo4j.common;

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
public class ProductWithCategory {
    private Category exactCategory;
    private Product product;
}