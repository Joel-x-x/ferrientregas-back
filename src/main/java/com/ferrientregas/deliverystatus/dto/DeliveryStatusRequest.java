package com.ferrientregas.deliverystatus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeliveryStatusRequest(
        @NotBlank
        @Size(min = 2, max = 30)
        String name
) {
}
