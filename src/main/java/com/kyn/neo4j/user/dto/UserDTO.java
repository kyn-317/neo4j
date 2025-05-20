package com.kyn.neo4j.user.dto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.kyn.neo4j.user.entity.User;
import com.kyn.neo4j.user.entity.FriendsRelationship;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID userId;
    private String name;
    private Double balance;
    private List<FriendDTO> friends;

    public static UserDTO from(User user) {
        if (user == null) return null;
        
        return UserDTO.builder()
            .userId(user.getUserId())
            .name(user.getName())
            .balance(user.getBalance())
            .friends(user.getFriends() != null ? 
                user.getFriends().stream()
                    .map(friendRel -> FriendDTO.from(friendRel.getTargetUser()))
                    .collect(Collectors.toList()) : 
                null)
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FriendDTO {
        private UUID userId;
        private String name;

        public static FriendDTO from(User user) {
            if (user == null) return null;
            
            return FriendDTO.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .build();
        }
    }
} 