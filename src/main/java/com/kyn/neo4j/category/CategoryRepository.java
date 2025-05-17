package com.kyn.neo4j.category;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveNeo4jRepository<Category, UUID> {
    
    @Query("MATCH (c:Category) WHERE c.name = $name RETURN c")
    Mono<Category> findByName(String name);

    @Query("MATCH (c:Category) WHERE c.elementId = $elementId RETURN c")
    Mono<Category> findByElementId(String elementId);

    @Query("MATCH (c:Category {name: $name}) " +
           "OPTIONAL MATCH (c)-[r:SUBCATEGORY]->(p:Category) " +
           "RETURN c, collect(r), collect(p)")
    Mono<Category> findByNameWithParent(String name);

    @Query("MATCH (c:Category {name: $name}) " +
           "OPTIONAL MATCH (c)<-[r:SUBCATEGORY]-(p:Category) " +
           "RETURN c, collect(r), collect(p)")
    Mono<Category> findByNameWithChildren(String name);
}
