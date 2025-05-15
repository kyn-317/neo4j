package com.kyn.neo4j.product;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    
    

    public Mono<Person> findByName(String name) {
        return personRepository.findByName(name);
    }

    public Flux<Person> findByTeammatesName(String name) {
        return personRepository.findByTeammatesName(name);
    }

    public Mono<Person> save(Person person) {
        return personRepository.save(person);
    }

    public Flux<Person> findAll() {
        return personRepository.findAll();
    }

    public Mono<Person> setTeammate(Person person, Person person2) {
        return this.findByName(person.getName())
            .flatMap(p -> 
            this.findByName(person2.getName()).map(p2 -> {
                    p.worksWith(p2);
                    return p;
                })
                .then(this.personRepository.save(p)));
    }

    public Mono<Person> setTeammateByName(String name1, String name2) {
        return this.findByName(name1)
            .flatMap(p -> 
                this.findByName(name2)
                .map(p2 -> {
                    p.worksWith(p2);
                    return p;
                })
                .then(this.personRepository.save(p)));
    }
}
