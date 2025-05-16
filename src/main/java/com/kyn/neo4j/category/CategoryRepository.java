package com.kyn.neo4j.category;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface CategoryRepository extends ReactiveNeo4jRepository<Category, UUID> {
    Mono<Category> findByName(String name);
}
