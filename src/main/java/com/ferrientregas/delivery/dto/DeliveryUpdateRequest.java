package com.ferrientregas.delivery.dto;

import com.ferrientregas.evidence.EvidenceEntity;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record DeliveryUpdateRequest(
        @NotNull
        UUID id,
        String numeration,
        String invoiceNumber,
        LocalDate deliveryDate,
        LocalTime estimateHourInit,
        LocalTime estimateHourEnd,
        String deliveryStatus,
        String paymentType,
        BigDecimal credit,
        BigDecimal total,
        List<EvidenceEntity> evidence,
        UUID userId,
        UUID customerId,
        String deliveryData,
        String observations,
        String comments
) {
}
