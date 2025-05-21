package com.kyn.neo4j.user.service;

import java.util.List;
import java.util.Map;

import org.neo4j.driver.Record;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Service;

import com.kyn.neo4j.user.dto.FriendDTO;
import com.kyn.neo4j.user.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class Neo4jTemplateService {
    
    private final ReactiveNeo4jClient neo4jClient;

    public Neo4jTemplateService(ReactiveNeo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }
    

    public Mono<UserDTO> findByName(String name) {
        return neo4jClient.query(
            """
            MATCH (u:User {name: $name})
            OPTIONAL MATCH (u)-[:FRIEND]->(f:User)
            WITH u, collect({
                userId: f.userId,
                name: f.name
            }) as friends
            RETURN {
                userId: u.userId,
                name: u.name,
                balance: u.balance,
                friends: friends
            } as result""")
            .bind(name)
            .to("name")
            .fetchAs(UserDTO.class)
            .mappedBy((TypeSystem typeSystem, Record record) -> {
                Map<String, Object> userData = record.get("result").asMap();
                @SuppressWarnings("unchecked")                
                List<Map<String, Object>> friendsData = (List<Map<String, Object>>) userData.get("friends");
                List<FriendDTO> friends = friendsData != null ? friendsData.stream()
                    .filter(friend -> friend.get("userId") != null && friend.get("name") != null)
                    .map(friend -> new FriendDTO(
                        (String) friend.get("userId"),
                        (String) friend.get("name")
                    ))
                    .toList() : List.of();
                return new UserDTO(
                    (String) userData.get("userId"),
                    (String) userData.get("name"),
                    userData.get("balance") != null ? ((Number) userData.get("balance")).doubleValue() : 0.0,
                    friends
                );
            })
            .one()
            .switchIfEmpty(Mono.empty());
    }
    
}
