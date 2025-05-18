package com.kyn.neo4j.product;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveNeo4jRepository<Product, UUID> {
    
    @Query("MATCH (p:Product) WHERE p.categoryString CONTAINS $categoryName RETURN p")
    Flux<Product> findByCategoryStringContaining(String categoryName);
    
    /**
     * Find a product by ID and load its category relationship
     */
    @Query("""
        MATCH (p:Product)
        WHERE p.id = $id
        OPTIONAL MATCH (p)-[r:BELONGS_TO]->(c:Category)
        RETURN p, collect(r), collect(c)
    """)
    Mono<Product> findByIdWithCategory(UUID id);
    
    /**
     * Find all products with their category relationships
     */
    @Query("""
        MATCH (p:Product)
        OPTIONAL MATCH (p)-[r:BELONGS_TO]->(c:Category)
        RETURN p, collect(r), collect(c)
    """)
    Flux<Product> findAllWithCategories();

}
