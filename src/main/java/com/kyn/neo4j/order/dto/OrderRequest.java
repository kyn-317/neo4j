package com.kyn.neo4j.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String orderId;
    private double price;
    private Integer quantity;
    private String productId;
    private String userId;
}
