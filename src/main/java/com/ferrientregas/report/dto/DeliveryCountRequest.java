package com.ferrientregas.report.dto;

public record DeliveryCountRequest(
        String deliveryStatus,
        String paymentType
) {
}
