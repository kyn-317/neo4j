package com.kyn.neo4j.product;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends ReactiveNeo4jRepository<Person, UUID> {


    Mono<Person> findByName(String name);
    Flux<Person> findByTeammatesName(String name);
  }