package com.kyn.neo4j.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductData {
    private String productName;
    private String description;
    private String categoryString;
    private String specification;
    private Double price;
    private String image;

}
