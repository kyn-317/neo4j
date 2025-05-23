package com.kyn.neo4j.common.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kyn.neo4j.order.dto.OrderDTO;
import com.kyn.neo4j.order.dto.OrderRequest;
import com.kyn.neo4j.order.service.interfaces.OrderService;
import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;
import com.kyn.neo4j.user.service.interfaces.UserService;

import reactor.core.publisher.Mono;


@RestController
@RequestMapping("user-order")
public class UserOrderController {

    private final UserService userService;
    private final OrderService orderService;

    public UserOrderController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping("create")
    public Mono<UserDTO> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("get/{name}")
    public Mono<UserDTO> getUserByName(@PathVariable String name) {
        return userService.getUserByName(name);
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

    @GetMapping("get-user-with-friends/{name}")
    public Mono<UserDTO> getUserWithFriends(@PathVariable String name) {
        return userService.getUserWithFriends(name);
    }
    

    @PostMapping("create-order")
    public Mono<OrderDTO> createOrder(@RequestBody OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("get-order/{orderId}")
    public Mono<OrderDTO> getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }
}

