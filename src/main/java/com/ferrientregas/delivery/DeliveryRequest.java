package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record DeliveryRequest (
        String numeration,
        String invoiceNumber,
        @NotNull
        String DeliveryDate,
        @NotNull
        LocalTime estimateHourInit,
        @NotNull
        LocalTime estimateHourEnd,
        @NotNull
        String deliveryStatusName,
        @NotNull
        String paymentType,
        @NotNull
        Double credit,
        @NotNull
        Double total,
        @NotNull
        List<EvidenceEntity> evidence,
        @NotNull
        UUID user,
        @NotNull
        UUID customer,
        String deliveryData,
        String observations,
        String comments
){
}
