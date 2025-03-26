package com.ferrientregas.firebase.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MultipleTokensRequest(
        @NotNull List<String> tokens,
        @NotBlank String title,
        @NotBlank String body
) {
}
