package com.kyn.neo4j.user;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

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


    public User() {}
    
}
