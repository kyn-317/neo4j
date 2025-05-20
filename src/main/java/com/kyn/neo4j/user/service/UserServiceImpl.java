package com.kyn.neo4j.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.FriendsRelationship;
import com.kyn.neo4j.user.entity.User;
import com.kyn.neo4j.user.repository.UserRepository;
import com.kyn.neo4j.user.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final User EMPTY_USER = new User();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> createUser(User user) {
        return this.getUserByName(user.getName())
            .switchIfEmpty(userRepository.save(user));
    }

    @Override
    public Mono<User> getUser(String userId) {
        return userRepository.findById(UUID.fromString(userId));
    }

    @Override
    public Mono<User> updateUser(User user) {
        return this.getUserByName(user.getName())
            .filter(existingUser -> existingUser.getName().equals(user.getName()))
            .flatMap(existingUser -> {
                final List<FriendsRelationship> existingFriends = existingUser.getFriends();
                existingUser.setBalance(user.getBalance());
                return userRepository.save(existingUser)
                    .flatMap(savedUser -> {
                        savedUser.setFriends(existingFriends);
                        return Mono.just(savedUser);
                    });
            })
            .switchIfEmpty(Mono.just(EMPTY_USER));
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public Mono<User> getUserByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public Mono<Void> deleteAllUser() {
        return userRepository.deleteAll();
    }

    @Override
    public Mono<User> addFriend(String userName, String friendName) {
        return Mono.zip(this.getUserByName(userName), this.getUserByName(friendName))
            .flatMap(tuple -> {
                User user = tuple.getT1();
                User friend = tuple.getT2();
                if(user == null || friend == null) {
                    return Mono.error(new RuntimeException("User or friend not found"));
                }
                
                final List<FriendsRelationship> existingFriends = user.getFriends() != null ? 
                    new ArrayList<>(user.getFriends()) : new ArrayList<>();
                
                FriendsRelationship newFriendRel = FriendsRelationship.builder()
                    .targetUser(friend)
                    .build();
                existingFriends.add(newFriendRel);
                user.setFriends(existingFriends);
                return userRepository.save(user)
                    .flatMap(savedUser -> {
                        savedUser.setFriends(existingFriends);
                        return Mono.just(savedUser);
                    });
            });
    }

    @Override
    public Mono<UserDTO> getUserWithFriends(String name) {
        return userRepository.findByNameWithFriends(name);
    }


}
