package com.ferrientregas.user.dto;

import com.ferrientregas.role.RoleEntity;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record UserResponse (
        UUID id,
        String firstNames,
        String lastNames,
        String email,
        String profileImage,
        Boolean emailConfirmed,
        Set<RoleEntity> roles
){
}
