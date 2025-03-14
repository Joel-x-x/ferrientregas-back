package com.ferrientregas.auth;

import com.ferrientregas.role.RoleEntity;
import lombok.Builder;

import java.util.Set;

@Builder
public record AuthenticationResponse (
        String accessToken,
        String refreshToken,
        Set<RoleEntity> roles,
        String verified
){
}
