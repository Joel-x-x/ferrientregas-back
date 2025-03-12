package com.ferrientregas.user;

import lombok.Builder;

@Builder
public record VerificationRequest (
        String token
){
}
