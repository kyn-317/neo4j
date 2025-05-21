package com.kyn.neo4j.order.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import com.kyn.neo4j.order.entity.Order;

public interface OrderRepository extends ReactiveNeo4jRepository<Order, UUID> {
    
}
