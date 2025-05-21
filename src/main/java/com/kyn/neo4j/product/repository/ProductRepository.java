package com.kyn.neo4j.product.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import com.kyn.neo4j.product.entity.Product;

@Repository
public interface ProductRepository extends ReactiveNeo4jRepository<Product, UUID> {

}
