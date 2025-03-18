package com.ferrientregas.delivery.dto;

import com.ferrientregas.delivery.DeliveryEntity;

public class DeliveryMapper {
    public static DeliveryResponse toDeliveryResponse(DeliveryEntity delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getNumeration(),
                delivery.getInvoiceNumber(),
                delivery.getDeliveryDate(),
                delivery.getEstimateHourInit(),
                delivery.getEstimateHourEnd(),
                delivery.getDeliveryStatus(),
                delivery.getPaymentType(),
                delivery.getCredit(),
                delivery.getTotal(),
                delivery.getEvidence(),
                delivery.getUser(),
                delivery.getCustomer(),
                delivery.getDeliveryData(),
                delivery.getObservations(),
                delivery.getComments()
        );
    }
}
