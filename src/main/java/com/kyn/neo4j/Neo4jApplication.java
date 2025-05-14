package com.kyn.neo4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories;


@SpringBootApplication
@EnableReactiveNeo4jRepositories
public class Neo4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(Neo4jApplication.class, args);
	}

}
