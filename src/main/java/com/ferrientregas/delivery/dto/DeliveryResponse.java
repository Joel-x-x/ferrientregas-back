package com.ferrientregas.delivery.dto;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Builder
public record DeliveryResponse (
        UUID id,
        String numeration,
        String invoiceNumber,
        java.time.LocalDate deliveryDate,
        @NotNull
        LocalTime estimateHourInit,
        @NotNull
        LocalTime estimateHourEnd,
        @NotNull
        DeliveryStatusEntity deliveryStatusName,
        @NotNull
        PaymentTypeEntity paymentType,
        @NotNull
        BigDecimal credit,
        @NotNull
        BigDecimal total,
        @NotNull
        List<EvidenceEntity> evidence,
        @NotNull
        UserEntity user,
        @NotNull
        CustomerEntity customer,
        String deliveryData,
        String observations,
        String comments
){
}
