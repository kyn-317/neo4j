package com.kyn.neo4j.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.order.dao.OrderDao;
import com.kyn.neo4j.order.dto.OrderDTO;
import com.kyn.neo4j.order.dto.OrderRequest;
import com.kyn.neo4j.order.repository.OrderRepository;
import com.kyn.neo4j.order.service.interfaces.OrderService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao;
    private final OrderRepository orderRepository;
    
    public OrderServiceImpl(OrderDao orderDao, OrderRepository orderRepository) {
        this.orderDao = orderDao;
        this.orderRepository = orderRepository;
    }   

    @Override
    public Mono<OrderDTO> createOrder(OrderRequest orderRequest) {
        log.info("Creating order: {}", orderRequest);
        return orderDao.createOrder(orderRequest);
    }

    @Override
    public Mono<OrderDTO> getOrderById(String orderId) {
        log.info("Getting order by id: {}", orderId);
        return orderDao.getOrderById(orderId);
    }
}
