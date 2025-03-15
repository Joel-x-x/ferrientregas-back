package com.ferrientregas.paymenttype.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PaymentTypeRequest(
        @NotBlank
        @Size(min = 2, max = 30)
        String name
) {
}
