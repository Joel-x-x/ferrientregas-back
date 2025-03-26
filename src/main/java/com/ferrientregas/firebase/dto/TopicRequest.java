package com.ferrientregas.firebase.dto;

import jakarta.validation.constraints.NotBlank;

public record TopicRequest(
        @NotBlank String topic,
        @NotBlank String title,
        @NotBlank String body
) {
}
