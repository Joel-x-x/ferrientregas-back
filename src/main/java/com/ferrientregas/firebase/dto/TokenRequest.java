package com.ferrientregas.firebase.dto;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank
        String token,
        @NotBlank
        String title,
        @NotBlank
        String body
) {
}
