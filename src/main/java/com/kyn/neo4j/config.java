package com.kyn.neo4j;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;

import reactor.core.publisher.Flux;

import org.neo4j.driver.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class config {
    
    private Logger log = LoggerFactory.getLogger(config.class);



    @Bean
	CommandLineRunner demo(PersonService personService) {
		return args -> {
			Person greg = new Person("Greg");
			Person roy = new Person("Roy");
			Person craig = new Person("Craig");

			Flux.just(greg, roy, craig)
				.flatMap(personService::save)
				.then(personService.setTeammateByName("Greg", "Roy"))
				.then(personService.setTeammateByName("Craig", "Roy"))
				.then(personService.setTeammateByName("Craig", "Chris"))
				.subscribe(savedPerson -> log.info("savedPerson: {}", savedPerson));			
				};
	}
}
