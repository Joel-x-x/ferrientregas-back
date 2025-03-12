package com.ferrientregas.user;

import lombok.Builder;

@Builder
public record VerificationResponse (
        String message,
        String token
){
}
