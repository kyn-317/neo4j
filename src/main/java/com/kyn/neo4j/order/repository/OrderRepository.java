package com.kyn.neo4j.order.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import com.kyn.neo4j.order.entity.Order;

@Repository
public interface OrderRepository extends ReactiveNeo4jRepository<Order, UUID> {
    
}
