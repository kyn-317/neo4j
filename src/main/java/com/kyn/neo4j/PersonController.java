package com.kyn.neo4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@RestController
@RequestMapping("/persons")
public class PersonController {
    
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }
    
    @PostMapping("/single")
    public Mono<Person> save(@RequestBody Person person) {
        return this.personService.save(person);
    }

    @GetMapping("/all")
    public Flux<Person> findAll() {
        return this.personService.findAll();
    }

    @GetMapping("/teammates/{name1}/{name2}")
    public Mono<Person> setTeammate(@PathVariable String name1, @PathVariable String name2) {
        return this.personService.setTeammate(new Person(name1), new Person(name2));
    }
    
    
}
