package com.ferrientregas.auth;

import lombok.Builder;

@Builder
public record RegisterRequest (
        String firstNames,
        String lastNames,
        String email,
        String password
){

}
