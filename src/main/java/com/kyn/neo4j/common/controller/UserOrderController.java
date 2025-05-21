package com.kyn.neo4j.common.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;
import com.kyn.neo4j.user.service.Neo4jTemplateService;
import com.kyn.neo4j.user.service.interfaces.UserService;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("user-order")
public class UserOrderController {

    private final UserService userService;
    private final Neo4jTemplateService neo4jTemplateService;

    public UserOrderController(UserService userService, Neo4jTemplateService neo4jTemplateService) {
        this.userService = userService;
        this.neo4jTemplateService = neo4jTemplateService;
    }

    @PostMapping("create")
    public Mono<User> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("get/{name}")
    public Mono<User> getUserByName(@PathVariable String name) {
        return userService.getUserByName(name);
    }

    @DeleteMapping("delete")
    public Mono<Void> deleteAllUser() {
        return userService.deleteAllUser();
    }

    @PostMapping("update")
    public Mono<User> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("add-friend/{userName}/{friendName}")
    public Mono<User> addFriend(@PathVariable String userName, @PathVariable String friendName) {
        return userService.addFriend(userName, friendName);
    }

    @GetMapping("get-user-with-friends/{name}")
    public Mono<UserDTO> getUserWithFriends(@PathVariable String name) {
        return userService.getUserWithFriends(name);
    }
    

    @GetMapping("get-user-with-friends-count/{name}")
    public Mono<UserDTO> getUserWithFriendsCount(@PathVariable String name) {
        return neo4jTemplateService.findByName(name);
    }
}

