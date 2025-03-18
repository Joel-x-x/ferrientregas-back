package com.ferrientregas.deliverystatus.dto;

import com.ferrientregas.deliverystatus.DeliveryStatusEntity;

public class DeliveryStatusMapper {
    public static  DeliveryStatusResponse toDeliveryStatusResponse(
            DeliveryStatusEntity deliveryStatus
    ){

        return new DeliveryStatusResponse(
                deliveryStatus.getId(),
                deliveryStatus.getName());
    }
}
