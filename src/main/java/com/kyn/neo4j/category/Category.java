package com.kyn.neo4j.category;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.neo4j.ogm.annotation.Index;

@Node("Category") 
public class Category {

    @Id @GeneratedValue 
    private Long id;

    @Property("name") 
    @Index(unique = true)
    private String name;


    @Relationship(type = "SUBCATEGORY", direction = Relationship.Direction.OUTGOING)
    private Category parentCategory; 

    public Category() {}

    public Category(String name) {
        this.name = name;
    }


    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

}