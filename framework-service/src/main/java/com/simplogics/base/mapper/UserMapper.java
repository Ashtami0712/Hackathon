package com.simplogics.base.mapper;

import com.simplogics.base.dto.UserResponse;
import com.simplogics.base.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse convertEntityToDto(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getIsActive())
                .roles(user.getUserRoles().stream().map(role ->
                        role.getRole().name()).collect(Collectors.toList()))
                .build();
    }

}
