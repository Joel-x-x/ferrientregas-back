package com.ferrientregas.driver.dto;

import com.ferrientregas.role.RoleEntity;

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
