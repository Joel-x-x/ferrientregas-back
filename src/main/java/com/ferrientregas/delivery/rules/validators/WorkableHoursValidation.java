package com.ferrientregas.delivery.rules.validators;

import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.exception.DeliveryBusinessLogicException;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class WorkableHoursValidation implements DeliveryBusinessLogicValidations {

    @Override
    public void validateInitHour(DeliveryEntity delivery)
    {
        LocalTime morningStartTime = LocalTime.of(8, 0);
        LocalTime morningEndTime = LocalTime.of(12, 0);
        LocalTime afternoonStartTime = LocalTime.of(13, 0);
        LocalTime nightEndTime = LocalTime.of(18, 0);

        LocalTime estimateInit = delivery.getEstimateHourInit();
        LocalTime estimateEnd = delivery.getEstimateHourEnd();
        LocalDate deliveryDate = delivery.getDeliveryDate();

        // Validar si la fecha de entrega es domingo
        if (deliveryDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            throw new DeliveryBusinessLogicException("Delivery date cannot be on a Sunday");
        }

        // Validar que la hora de inicio esté dentro del horario permitido
        if (estimateInit.isBefore(morningStartTime) ||
                (estimateInit.isAfter(morningEndTime) && estimateInit.isBefore(afternoonStartTime)) ||
                estimateInit.isAfter(nightEndTime)) {
            throw new DeliveryBusinessLogicException("Estimate hour init must be within working hours (08:00 - 12:00, 13:00 - 18:00)");
        }

        // Validar que la hora de fin esté dentro del horario permitido
        if (estimateEnd.isBefore(morningStartTime) ||
                (estimateEnd.isAfter(morningEndTime) && estimateEnd.isBefore(afternoonStartTime)) ||
                estimateEnd.isAfter(nightEndTime)) {
            throw new DeliveryBusinessLogicException("Estimate hour end must be within working hours (08:00 - 12:00, 13:00 - 18:00)");
        }

        // Validar que la hora de inicio no sea después de la hora de fin
        if (estimateInit.isAfter(estimateEnd)) {
            throw new DeliveryBusinessLogicException("Estimate hour init cannot be after estimate hour end");
        }

        // Validar que haya al menos 30 minutos entre la hora de inicio y fin
        if (estimateInit.plusMinutes(30).isAfter(estimateEnd)) {
            throw new DeliveryBusinessLogicException("Delivery time range must be at least 30 minutes");
        }
    }
}
