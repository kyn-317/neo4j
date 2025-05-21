package com.kyn.neo4j.user.service.interfaces;

import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;

import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<UserDTO> createUser(User user);
    public Mono<UserDTO> getUser(String userId);
    public Mono<UserDTO> getUserByName(String name);
    public Mono<UserDTO> updateUser(User user);
    public Mono<Void> deleteUser(String userId);
    public Mono<Void> deleteAllUser();
    public Mono<UserDTO> getUserWithFriends(String name);
    public Mono<UserDTO> addFriend(String userName, String friendName);
}
