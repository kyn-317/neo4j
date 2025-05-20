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
            OPTIONAL MATCH (u)-[:FRIEND]->(r:User)
            WITH u, collect(CASE WHEN r IS NOT NULL THEN {userId: r.userId, name: r.name} ELSE NULL END) as friendList
            RETURN {
                userId: u.userId,
                name: u.name,
                balance: u.balance,
                friends: [f IN friendList WHERE f IS NOT NULL]
            } as userDTO
            """)
    Mono<UserDTO> findByNameWithFriends(String name);

}
