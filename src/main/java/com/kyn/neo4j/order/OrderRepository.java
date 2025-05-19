package com.kyn.neo4j.order;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface OrderRepository extends ReactiveNeo4jRepository<Order, UUID> {
    
}
