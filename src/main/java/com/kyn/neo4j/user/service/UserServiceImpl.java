package com.kyn.neo4j.user.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kyn.neo4j.user.dao.UserDao;
import com.kyn.neo4j.user.dto.UserDTO;
import com.kyn.neo4j.user.entity.User;
import com.kyn.neo4j.user.repository.UserRepository;
import com.kyn.neo4j.user.service.interfaces.UserService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDao neo4jTemplateService;

    public UserServiceImpl(UserRepository userRepository, UserDao neo4jTemplateService) {
        this.userRepository = userRepository;
        this.neo4jTemplateService = neo4jTemplateService;
    }

    @Override
    public Mono<UserDTO> createUser(User user) {
        return neo4jTemplateService.saveUser(user);
    }

    @Override
    public Mono<UserDTO> getUser(String userId) {
        return neo4jTemplateService.findById(UUID.fromString(userId));
    }

    @Override
    public Mono<UserDTO> updateUser(User user) {
        return neo4jTemplateService.updateUserBalance(user);
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        return userRepository.deleteById(UUID.fromString(userId));
    }

    @Override
    public Mono<UserDTO> getUserByName(String name) {
        return neo4jTemplateService.findByNameWithFriends(name);
    }

    @Override
    public Mono<Void> deleteAllUser() {
        return userRepository.deleteAll();
    }

    @Override
    public Mono<UserDTO> getUserWithFriends(String name) {
        return neo4jTemplateService.findByNameWithFriends(name);
    }

    @Override
    public Mono<UserDTO> addFriend(String userName, String friendName) {
        return neo4jTemplateService.addFriend(userName, friendName);
    }

}
