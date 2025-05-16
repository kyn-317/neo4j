package com.kyn.neo4j.person;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.Data;

@Node("Person")
@Data
public class Person {

  @Id @GeneratedValue private UUID elementId;

  private String name;

  public Person() {}

  public Person(String name) {
    this.name = name;
  }

  @Relationship(type = "TEAMMATE")
  public Set<Person> teammates;

  public void worksWith(Person person) {
    if (teammates == null) {
      teammates = new HashSet<>();
    }
    teammates.add(person);
  }

}