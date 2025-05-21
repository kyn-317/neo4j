package com.kyn.neo4j.user.repository;

import java.util.UUID;

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveNeo4jRepository<User, UUID> {
    
    Mono<User> findByName(String name);

    @Query("""
            MATCH (u:User {name: $name})
            MATCH (u)-[:FRIEND]->(f:User)
            WITH u, collect({
                userId: f.userId,
                name: f.name
            }) as friends
            RETURN {
                userId: u.userId,
                name: u.name,
                balance: u.balance,
                friends: friends
            }
            """)
    Mono<UserDTO> findByNameWithFriends(String name);
}