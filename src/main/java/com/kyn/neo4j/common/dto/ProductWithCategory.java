package com.kyn.neo4j.common.dto;

import com.kyn.neo4j.category.entity.Category;
import com.kyn.neo4j.product.entity.Product;

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