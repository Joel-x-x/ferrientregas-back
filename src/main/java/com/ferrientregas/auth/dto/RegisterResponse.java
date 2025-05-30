package com.ferrientregas.auth.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record RegisterResponse(
        UUID id,
        String firstNames,
        String lastNames,
        String email,
        Boolean emailConfirmed
){

}
