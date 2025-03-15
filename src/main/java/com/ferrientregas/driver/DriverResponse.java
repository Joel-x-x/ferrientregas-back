package com.ferrientregas.driver;

import com.ferrientregas.role.RoleEntity;
import lombok.Builder;

import java.util.Set;

//@Builder
public record DriverResponse (
    String firstNames,
    String lastNames,
    String email,
    String profileImage,
    String token,
    Set<RoleEntity> roles
){
}
