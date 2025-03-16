package com.ferrientregas.deliverystatus.dto;

import java.util.UUID;

public record DeliveryStatusResponse(
        UUID id,
        String name
) {
}
