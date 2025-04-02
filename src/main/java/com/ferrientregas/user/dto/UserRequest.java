package com.ferrientregas.user.dto;

public record UserRequest (
        String firstNames,
        String lastNames,
        String email,
        String password,
        String profileImage,
        Boolean emailConfirmed,
        String role
){
}
