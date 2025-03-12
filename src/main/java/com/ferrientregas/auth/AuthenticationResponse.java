package com.ferrientregas.auth;

import com.ferrientregas.role.RoleEntity;
import lombok.Builder;

@Builder
public record AuthenticationResponse (
        String token,
        RoleEntity role
){
}
