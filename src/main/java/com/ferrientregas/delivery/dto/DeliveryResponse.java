package com.ferrientregas.delivery.dto;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalTime;
import java.util.List;

@Builder
public record DeliveryResponse (

        String numeration,
        String invoiceNumber,
        @NotNull
        String deliveryDate,
        @NotNull
        LocalTime estimateHourInit,
        @NotNull
        LocalTime estimateHourEnd,
        @NotNull
        DeliveryStatusEntity deliveryStatusName,
        @NotNull
        PaymentTypeEntity paymentType,
        @NotNull
        Double credit,
        @NotNull
        Double total,
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
