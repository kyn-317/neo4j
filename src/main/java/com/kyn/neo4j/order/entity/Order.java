package com.kyn.neo4j.order.entity;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.kyn.neo4j.product.entity.Product;
import com.kyn.neo4j.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Node("Order")
public class Order {
    @Id
    @GeneratedValue
    private UUID orderId;

    @Property("price")
    private Double price;

    @Property("quantity")
    private Integer quantity;

    @Relationship(type="ORDERED_USER", direction = Relationship.Direction.OUTGOING)
    private User user;

    @Relationship(type="ORDERED_PRODUCT", direction = Relationship.Direction.OUTGOING)
    private Product product;
}
