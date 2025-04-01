package com.ferrientregas.delivery.dto;

import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.evidence.EvidenceEntity;
import com.ferrientregas.evidence.dto.EvidenceResponse;
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
        LocalTime estimateHourInit,
        LocalTime estimateHourEnd,
        DeliveryStatusEntity deliveryStatus,
        PaymentTypeEntity paymentType,
        BigDecimal credit,
        BigDecimal total,
        List<EvidenceResponse> evidence,
        UserEntity user,
        CustomerEntity customer,
        String deliveryData,
        String observations,
        String comments
){
}
