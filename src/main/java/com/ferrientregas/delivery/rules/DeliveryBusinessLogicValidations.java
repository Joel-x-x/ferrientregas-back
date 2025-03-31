package com.ferrientregas.delivery.rules;

import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.exception.DeliveryBusinessLogicException;

import java.time.LocalDate;
import java.time.LocalTime;

public interface DeliveryBusinessLogicValidations {
    void validateInitHour(DeliveryEntity delivery);
}
