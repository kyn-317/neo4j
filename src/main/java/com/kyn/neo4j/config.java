package com.kyn.neo4j;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kyn.neo4j.product.Person;
import com.kyn.neo4j.product.PersonService;

import reactor.core.publisher.Flux;

@Configuration
public class config {
    
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
				.subscribe();			
				};
	}
}
