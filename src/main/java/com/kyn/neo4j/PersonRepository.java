package com.kyn.neo4j;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

    //Mono<Person> findByName(String name);
    //Flux<Person> findByTeammatesName(String name);

    Person findByName(String name);
    List<Person> findByTeammatesName(String name);
  }