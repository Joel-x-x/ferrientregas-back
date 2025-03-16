package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.paymenttype.PaymentTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

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
        UserEntity user,
        @NotNull
        CustomerEntity customer,
        String deliveryData,
        String observations,
        String comments
){
}
