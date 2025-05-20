package com.kyn.neo4j.order;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.kyn.neo4j.product.Product;
import com.kyn.neo4j.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
    @JsonBackReference
    private User user;

    @Relationship(type="ORDERED_PRODUCT", direction = Relationship.Direction.OUTGOING)
    @JsonManagedReference
    private Product product;
}
