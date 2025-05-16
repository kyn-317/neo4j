package com.kyn.neo4j.category;

import java.util.UUID;

import org.neo4j.ogm.annotation.Index;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Node("Category") 
@AllArgsConstructor
@Builder
@Data
public class Category {

    @Id @GeneratedValue 
    private UUID id;

    @Property("name") 
    @Index(unique = true)
    private String name;


    @Relationship(type = "SUBCATEGORY", direction = Relationship.Direction.OUTGOING)
    private Category parentCategory; 

    public Category() {}

    public Category(String name) {
        this.name = name;
    }



}