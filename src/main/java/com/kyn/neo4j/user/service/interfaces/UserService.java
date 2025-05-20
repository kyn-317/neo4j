package com.kyn.neo4j.user.service.interfaces;

import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;

import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<User> createUser(User user);
    public Mono<User> getUser(String userId);
    public Mono<User> getUserByName(String name);
    public Mono<UserDTO> updateUser(User user);
    public Mono<Void> deleteUser(String userId);
    public Mono<Void> deleteAllUser();

    public Mono<UserDTO> addFriend(String userId, String friendId);
}
