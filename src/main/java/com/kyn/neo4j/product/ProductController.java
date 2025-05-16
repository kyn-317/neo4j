package com.kyn.neo4j.product;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.neo4j.service.ProductInsertService;

import reactor.core.publisher.Mono;



@RestController

@RequestMapping("product")
public class ProductController {
    private final ProductInsertService productInsertService;

    public ProductController(ProductInsertService productInsertService) {
        this.productInsertService = productInsertService;
    }

    @PostMapping
    public Mono<Product> postMethodName(@RequestBody ProductData entity) {
        return productInsertService.createProduct(entity);
        
    }
    
}
