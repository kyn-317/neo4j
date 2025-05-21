package com.kyn.neo4j.order.dao;

import java.util.Map;
import java.util.UUID;

import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;

import com.kyn.neo4j.order.dto.OrderDTO;
import com.kyn.neo4j.order.dto.OrderRequest;
import com.kyn.neo4j.order.dto.UserAccount;
import com.kyn.neo4j.order.repository.OrderRepository;
import com.kyn.neo4j.product.entity.Product;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class OrderDao {
    private final OrderRepository orderRepository;
    private final ReactiveNeo4jClient client;

    public OrderDao(OrderRepository orderRepository, ReactiveNeo4jClient client) {
        this.orderRepository = orderRepository;
        this.client = client;
    }
    

    public Mono<OrderDTO> createOrder(OrderRequest orderRequest) {
        orderRequest.setOrderId(UUID.randomUUID().toString());
        return client.query("""
                Match( product:Product {id: $productId})
                Match( user:User {userId: $userId})
                Create ( order : Order {orderId: $orderId, price: $price, quantity: $quantity})
                merge (order)-[:ORDERED_PRODUCT]->(product)
                merge (order)-[:ORDERED_USER]->(user)
                return order
                """).bindAll(Map.of(
                    "productId", orderRequest.getProductId(),
                    "userId", orderRequest.getUserId(),
                    "orderId", orderRequest.getOrderId(),
                    "price", orderRequest.getPrice(),
                    "quantity", orderRequest.getQuantity()
                )).fetch().one().then(getOrderById(orderRequest.getOrderId()));
    }

    public Mono<OrderDTO> getOrderById(String orderId) {
        return client.query("""
                Match (order:Order {orderId: $orderId})
                Match (order)-[:ORDERED_USER]->(user:User)
                Match (order)-[:ORDERED_PRODUCT]->(product:Product)
                return {
                    orderId: order.orderId,
                    price: order.price,
                    quantity: order.quantity,
                    user: {
                        userId: user.userId,
                        name: user.name,
                        balance: user.balance
                    },
                    product: {
                        id: product.id,
                        name: product.name,
                        description: product.description,
                        sellingPrice: product.sellingPrice,
                        productSpecification: product.productSpecification,
                        image: product.image,
                        categoryString: product.categoryString
                    }
                } as result
                """).bindAll(Map.of("orderId", orderId))
                .fetchAs(OrderDTO.class)
                .mappedBy((typeSystem, record) -> {
                    Map<String, Object> result = record.get("result").asMap();
                    @SuppressWarnings("unchecked")
                    Map<String, Object> userData = (Map<String, Object>) result.get("user");
                    @SuppressWarnings("unchecked")
                    Map<String, Object> productData = (Map<String, Object>) result.get("product");
                    UserAccount user = UserAccount.builder()
                        .userId((String) userData.get("userId"))
                        .name((String) userData.get("name"))
                        .balance(userData.get("balance") != null ? 
                                ((Number) userData.get("balance")).doubleValue() : 0.0)
                        .build();
                    Product product = Product.builder()
                        .id(UUID.fromString((String) productData.get("id")))
                        .name((String) productData.get("name"))
                        .description((String) productData.get("description"))
                        .sellingPrice(productData.get("sellingPrice") != null ? 
                                ((Number) productData.get("sellingPrice")).doubleValue() : 0.0)
                        .productSpecification((String) productData.get("productSpecification"))
                        .image((String) productData.get("image"))
                        .categoryString((String) productData.get("categoryString"))
                        .build();
                    return OrderDTO.builder()
                        .orderId((String) result.get("orderId"))
                        .price(result.get("price") != null ? 
                               ((Number) result.get("price")).doubleValue() : 0.0)
                        .quantity(result.get("quantity") != null ? 
                                 ((Number) result.get("quantity")).intValue() : 0)
                        .user(user)
                        .product(product)
                        .build();
                })
                .one();
    }
}
