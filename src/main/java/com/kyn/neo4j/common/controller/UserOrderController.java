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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("user-order")
public class UserOrderController {

    private final UserService userService;

    public UserOrderController(UserService userService) {
        this.userService = userService;
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
    

}

