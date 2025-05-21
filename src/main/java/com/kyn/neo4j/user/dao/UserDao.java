package com.kyn.neo4j.user.dao;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.neo4j.driver.Record;
import org.neo4j.driver.types.TypeSystem;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.kyn.neo4j.user.dto.FriendDTO;
import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UserDao {
    
    private final ReactiveNeo4jClient neo4jClient;

    public UserDao(ReactiveNeo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }
    

    public Mono<UserDTO> findByNameWithFriends(String name) {
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
                List<FriendDTO> friends = friendsData != null ?
                    friendsData.stream()
                        .filter(friend -> friend.get("userId") != null && friend.get("name") != null)
                        .map(friend -> new FriendDTO(
                            (String) friend.get("userId"),
                            (String) friend.get("name")
                    )).toList() :
                    List.of();
                return UserDTO.builder()
                    .userId((String) userData.get("userId"))
                    .name((String) userData.get("name"))
                    .balance(userData.get("balance") != null ?
                                 ((Number) userData.get("balance")).doubleValue() :
                                 0.0
                            )
                    .friends(friends)
                    .build();
            })
            .one()
            .switchIfEmpty(Mono.empty());
    }
    

    public Mono<UserDTO> addFriend(String userName, String friendName) {
        return neo4jClient.query(
            """
            MATCH (u:User {name: $userName})
            MATCH (f:User {name: $friendName})
            MERGE (u)-[:FRIEND]->(f)
            """)
            .bind(userName)
            .to("userName")
            .bind(friendName)
            .to("friendName")
            .run()
            .then(this.findByNameWithFriends(userName));
    }

    public Mono<UserDTO> saveUser(User user) {
        return neo4jClient.query(
            """
            CREATE (u:User 
            {
             userId: $userId,
             name: $name,
             balance: $balance
            })
            RETURN 
            u.userId as userId,
            u.name as name,
            u.balance as balance
            """
        )
        .bind(UUID.randomUUID().toString())
        .to("userId")
        .bind(user.getName())
        .to("name")
        .bind(user.getBalance())
        .to("balance")  
        .fetchAs(UserDTO.class)
        .mappedBy((TypeSystem typeSystem, Record record) -> {
            return UserDTO.builder()
                .userId(record.get("userId").asString())
                .name(record.get("name").asString())
                .balance(record.get("balance").asDouble())
                .friends(List.of())
                .build();
        })
        .one()
        .switchIfEmpty(Mono.empty());
    }

    public Mono<UserDTO> updateUserBalance(User user) {
        return neo4jClient.query(
            """
            MATCH (u:User {userId: $userId})
            SET u.balance = $balance
            """ 
        )
        .bind(user.getUserId().toString())
        .to("userId")
        .bind(user.getBalance())
        .to("balance")
        .run()
        .then(this.findByNameWithFriends(user.getName()));
    }

    public Mono<UserDTO> findById(UUID userId) {
        return neo4jClient.query(
            """
            MATCH (u:User {userId: $userId})
            RETURN u.userId as userId, u.name as name, u.balance as balance
            """ 
        )
        .bind(userId)
        .to("userId")
        .fetchAs(UserDTO.class)
        .mappedBy((TypeSystem typeSystem, Record record) -> {
            return UserDTO.builder()
                .userId(record.get("userId").asString())
                .name(record.get("name").asString())
                .balance(record.get("balance").asDouble())
                .friends(List.of())
                .build();
        })
        .one()
        .switchIfEmpty(Mono.empty());
    }


    public Mono<Boolean> pay(String userId, Double amount) {
        return neo4jClient.query(
            """
            MATCH (u:User {userId: $userId})
            WITH u, u.balance as currentBalance, 
                 CASE 
                    WHEN currentBalance < $amount THEN 'INSUFFICIENT_BALANCE'
                    ELSE 'SUCCESS'
                 END as status
            WHERE status = 'SUCCESS'
            SET u.balance = currentBalance - $amount
            RETURN status
            """
        )
        .bind(userId)
        .to("userId")
        .bind(amount)
        .to("amount")
        .fetchAs(String.class)
        .mappedBy((TypeSystem typeSystem, Record record) -> 
            record.get("status").asString())
        .one()
        .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
        .flatMap(status -> {
            if ("INSUFFICIENT_BALANCE".equals(status)) {
                return Mono.error(new RuntimeException("Insufficient balance"));
            }
            return Mono.just(true);
        });
    }
}
