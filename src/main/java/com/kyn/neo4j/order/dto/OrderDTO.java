package com.kyn.neo4j.order.dto;



import com.kyn.neo4j.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private String orderId;
    private Double price;
    private Integer quantity;
    private UserAccount user;
    private Product product;
}
