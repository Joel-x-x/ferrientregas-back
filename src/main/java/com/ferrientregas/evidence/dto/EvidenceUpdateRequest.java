package com.ferrientregas.evidence.dto;

import jakarta.validation.constraints.NotNull;

public record EvidenceUpdateRequest(
        @NotNull
       String url
) {
}
