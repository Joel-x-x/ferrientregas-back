package com.ferrientregas.user.dto;

import lombok.Builder;

@Builder
public record VerificationRequest (
        String token
){
}
