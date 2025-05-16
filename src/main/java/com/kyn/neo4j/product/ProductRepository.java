package com.kyn.neo4j.product;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveNeo4jRepository<Product, UUID> {
    
}
