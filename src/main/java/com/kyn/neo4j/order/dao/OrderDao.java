package com.kyn.neo4j.order.dao;

import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;

import com.kyn.neo4j.order.dto.OrderDTO;
import com.kyn.neo4j.order.dto.OrderRequest;
import com.kyn.neo4j.order.entity.Order;
import com.kyn.neo4j.order.repository.OrderRepository;

import reactor.core.publisher.Mono;

@Component
public class OrderDao {
    private final OrderRepository orderRepository;
    private final ReactiveNeo4jClient client;

    public OrderDao(OrderRepository orderRepository, ReactiveNeo4jClient client) {
        this.orderRepository = orderRepository;
        this.client = client;
    }
    

    public Mono<OrderDTO> createOrder(OrderRequest orderRequest) {
        return client.query("""
                Match( product:Product {id: $productId})
                Match( user:User {id: $userId})
                Create ( order : Order {orderId: $orderId, price: $price, quantity: $quantity})
                merge (order)-[:ORDERED_PRODUCT]->(product)
                merge (order)-[:ORDERED_USER]->(user)
                return order
                """).fetch().one().then(Mono.empty());
    }
}
