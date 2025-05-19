package com.kyn.neo4j;

import org.neo4j.driver.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;



@Configuration
public class ReactiveNeo4jConfig {
    
	@Bean
	public ReactiveNeo4jTransactionManager reactiveTransactionManager(Driver driver) {
		return new ReactiveNeo4jTransactionManager(driver);
	}


    
}
