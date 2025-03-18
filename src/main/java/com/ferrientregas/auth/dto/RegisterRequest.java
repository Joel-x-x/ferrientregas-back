package com.ferrientregas.auth.dto;

import lombok.Builder;

@Builder
public record RegisterRequest (
        String firstNames,
        String lastNames,
        String email,
        String password
){

}
