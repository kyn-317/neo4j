package com.kyn.neo4j.order.service.interfaces;

import com.kyn.neo4j.order.dto.OrderDTO;
import com.kyn.neo4j.order.dto.OrderRequest;

import reactor.core.publisher.Mono;

public interface OrderService {
    Mono<OrderDTO> createOrder(OrderRequest orderRequest);
    Mono<OrderDTO> getOrderById(String orderId);
}
