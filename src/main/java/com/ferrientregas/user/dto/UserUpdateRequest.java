package com.ferrientregas.user.dto;

import java.util.UUID;

public record UserUpdateRequest (

        String firstNames,
        String lastNames,
        String email,
        String password,
        UUID profileImage,
        Boolean emailConfirmed,
        String role
){
}
