package com.ferrientregas.paymenttype.dto;

import java.util.UUID;

public record PaymentTypeResponse(
        UUID id,
        String name
) {
}
