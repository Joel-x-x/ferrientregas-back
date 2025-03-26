package com.ferrientregas.firebase.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.RequestBody;

public record TokenRequest(
        @NotBlank
        String token,
        @NotBlank
        String title,
        @NotBlank
        String body
) {
}
