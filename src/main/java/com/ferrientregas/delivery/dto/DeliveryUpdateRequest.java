package com.ferrientregas.delivery.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record DeliveryUpdateRequest(
        String numeration,
        String invoiceNumber,
        LocalDate deliveryDate,
        LocalTime estimateHourInit,
        LocalTime estimateHourEnd,
        String deliveryStatus,
        String paymentType,
        BigDecimal credit,
        BigDecimal total,
        UUID userId,
        UUID customerId,
        String deliveryData,
        String observations,
        String comments
) {
}
