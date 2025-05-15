package com.kyn.neo4j.person;

import java.util.Locale.Category;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node("Product") 
public class Product {

    @Id @GeneratedValue 
    private Long id;

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
    
    @Relationship(type = "BELONGS_TO", direction = Relationship.Direction.OUTGOING)
    private Category mostSpecificCategory; 

    public Product() {}

    public Product(String name, String description, double sellingPrice, String productSpecification, String image) {
        this.name = name;
        this.description = description;
        this.sellingPrice = sellingPrice;
        this.productSpecification = productSpecification;
        this.image = image;
    }

    public Category getMostSpecificCategory() {
        return mostSpecificCategory;
    }

    public void setMostSpecificCategory(Category mostSpecificCategory) {
        this.mostSpecificCategory = mostSpecificCategory;
    }
}