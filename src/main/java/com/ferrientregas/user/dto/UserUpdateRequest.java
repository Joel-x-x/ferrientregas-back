package com.ferrientregas.user.dto;

public record UserUpdateRequest (
        String firstNames,
        String lastNames,
        String email,
        String profileImage,
        Boolean emailConfirmed
){
}
