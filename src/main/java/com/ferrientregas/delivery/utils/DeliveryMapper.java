package com.ferrientregas.delivery.utils;

import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.evidence.dto.EvidenceMapper;
import com.ferrientregas.user.UserMapper;

import java.util.stream.Collectors;

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
                delivery.getEvidence()
                        .stream()
                        .map(EvidenceMapper::toEvidenceResponse)
                        .collect(Collectors.toList()),
                UserMapper.toUserResponse(delivery.getUser()),
                delivery.getCustomer(),
                delivery.getDeliveryData(),
                delivery.getObservations(),
                delivery.getComments()
        );
    }
}
