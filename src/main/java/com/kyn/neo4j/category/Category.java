package com.kyn.neo4j.category;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Node
@Builder
@AllArgsConstructor
public class Category {
    @Id
    private String elementId;  // Changed from UUID to String for elementId

    private String name;

    @Relationship(type = "SUBCATEGORY", direction = Relationship.Direction.OUTGOING)
    private Category parentCategory;

    public Category() {}
}