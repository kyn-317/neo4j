package com.kyn.neo4j.category;

import java.util.UUID;


import org.neo4j.ogm.annotation.Property;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Node("Category")
@Builder
@AllArgsConstructor
public class Category {
    

    @Id @GeneratedValue 
    private UUID id;

    @Property("name")
    private String name;

    @Relationship(type = "SUBCATEGORY", direction = Direction.INCOMING)
    private Category parentCategory;

    @Relationship(type = "SUBCATEGORY", direction = Direction.OUTGOING)
    private Category childCategory;

    public Category() {}
}