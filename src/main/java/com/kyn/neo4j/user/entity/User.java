package com.kyn.neo4j.user.entity;

import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Node("User")
public class User {

    @Id
    @GeneratedValue
    private UUID userId;

    @Property("name")
    private String name;

    @Property("balance")
    private Double balance;

    @Relationship(type="FRIEND", direction = Relationship.Direction.OUTGOING)
    private List<FriendsRelationship> friends;

    public User() {}
    
}
