package com.kyn.neo4j.user;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

public interface UserRepository extends ReactiveNeo4jRepository<User, UUID> {
    
}
