package com.kyn.neo4j.product.entity;

import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.kyn.neo4j.category.entity.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Node("Product") 
@AllArgsConstructor
@Builder
@Data
public class Product {

    @Id @GeneratedValue 
    private UUID id;

    @Property("name") 
    private String name; 

    @Property("description")
    private String description;

    @Property("sellingPrice")
    private double sellingPrice;

    @Property("productSpecification")
    private String productSpecification; 

    @Property("image")
    private String image;
    
    @Relationship(type = "BELONGS_TO", direction = Direction.OUTGOING)
    private Category category;

    @Property("categoryString")
    private String categoryString;

    public Product() {}
}