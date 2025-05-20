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
import com.kyn.neo4j.user.service.interfaces.UserService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("user-order")
public class UserOrderController {

    private final UserService userService;

    public UserOrderController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("create")
    public Mono<UserDTO> createUser(@RequestBody User user) {
        return userService.createUser(user)
            .map(UserDTO::from);
    }

    @GetMapping("get/{name}")
    public Mono<UserDTO> getUserByName(@PathVariable String name) {
        return userService.getUserByName(name)
            .map(UserDTO::from);
    }

    @DeleteMapping("delete")
    public Mono<Void> deleteAllUser() {
        return userService.deleteAllUser();
    }

    @PostMapping("update")
    public Mono<UserDTO> updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("add-friend/{userName}/{friendName}")
    public Mono<UserDTO> addFriend(@PathVariable String userName, @PathVariable String friendName) {
        return userService.addFriend(userName, friendName);
    }

}

