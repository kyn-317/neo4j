package com.kyn.neo4j.user.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

import com.kyn.neo4j.user.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveNeo4jRepository<User, UUID> {
    Mono<User> findByName(String name);
}
