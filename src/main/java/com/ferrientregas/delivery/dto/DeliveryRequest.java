package com.ferrientregas.delivery.dto;

import com.ferrientregas.evidence.EvidenceEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record DeliveryRequest (
        @NotBlank
        String numeration,
        String invoiceNumber,
        @NotNull
        LocalDate deliveryDate,
        @NotNull
        LocalTime estimateHourInit,
        @NotNull
        LocalTime estimateHourEnd,
        @NotNull
        String deliveryStatus,
        @NotNull
        String paymentType,
        @NotNull
        BigDecimal credit,
        @NotNull
        BigDecimal total,
        @NotNull
        List<EvidenceEntity> evidence,
        @NotNull
        UUID userId,
        UUID customerId,
        String deliveryData,
        String observations,
        String comments
) {
}
