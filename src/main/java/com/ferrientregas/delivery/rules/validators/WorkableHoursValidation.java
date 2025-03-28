package com.ferrientregas.delivery.rules.validators;

import com.ferrientregas.delivery.exception.DeliveryBusinessLogicException;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalTime.now;

public class WorkableHoursValidation implements DeliveryBusinessLogicValidations {
    @Override
    public void validateInitHour(LocalDate deliveryDate,
                                 List<DeliveryEntity> pendingDeliveries)
    throws DeliveryBusinessLogicException {

        LocalTime morningStartTime = LocalTime.of(8, 0);
        LocalTime morningEndTime = LocalTime.of(12,0);
        LocalTime afternoonStartTime = LocalTime.of(13,0);
        LocalTime nightEndTime = LocalTime.of(18,0);

        LocalDate deliveryDateSet = deliveryDate;

        if(pendingDeliveries.isEmpty()) {
            if(now().isAfter(morningEndTime)) {
                throw new DeliveryBusinessLogicException(
                        "Lunch Time"
                );
            }
            if(now().isAfter(nightEndTime)) {
                deliveryDateSet = deliveryDateSet.plusDays(1);
                throw new DeliveryBusinessLogicException(
                        "Try tomorrow we're not working at this time"
                );
            }
            if(now().isBefore(morningStartTime)) {
                throw new DeliveryBusinessLogicException(
                        "Try later we didn't even open yet"
                );
            }
        }


        // Filtering by the same day
        LocalTime lastDeliveryEnd = pendingDeliveries.get(pendingDeliveries
                .size() - 1).getEstimateHourEnd();

        if (lastDeliveryEnd.isAfter(nightEndTime)) {
            deliveryDateSet = deliveryDateSet.plusDays(1);
            throw new DeliveryBusinessLogicException(
                    "Try tomorrow we're not working at this time"
            );
        }
    }
}
