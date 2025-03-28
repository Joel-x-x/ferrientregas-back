package com.ferrientregas.delivery.rules;

import com.ferrientregas.delivery.exception.DeliveryBusinessLogicException;
import com.ferrientregas.delivery.DeliveryEntity;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryBusinessLogicValidations {
    void validateInitHour(LocalDate deliveryDate,
                          List<DeliveryEntity> pendingDeliveries) throws
            DeliveryBusinessLogicException;
}
