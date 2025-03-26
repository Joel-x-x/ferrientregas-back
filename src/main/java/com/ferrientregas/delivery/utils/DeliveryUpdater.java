package com.ferrientregas.delivery.utils;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class DeliveryUpdater {
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private static final Set<String> DELIVERY_STATUS =
            Set.of("PENDIENTE","EN PROCESO", "INCOMPLETO", "ENTREGADO",
                    "ATRASADO","CANCELADO");

    public void updateDeliveryFields(DeliveryEntity delivery,
                                      DeliveryUpdateRequest deliveryRequest) {

        if(!StringUtils.isBlank(deliveryRequest.numeration())) {
            delivery.setNumeration(deliveryRequest.numeration());
        }
        if(!StringUtils.isBlank(deliveryRequest.invoiceNumber())) {
            delivery.setInvoiceNumber(deliveryRequest.invoiceNumber());
        }
        if(deliveryRequest.deliveryDate() != null) {
            delivery.setDeliveryDate(deliveryRequest.deliveryDate());
        }
        delivery.setEstimateHourInit(calculateEstimateHourInit(deliveryRequest));
        delivery.setEstimateHourEnd(deliveryRequest.estimateHourInit().plusHours(1));
        delivery.setDeliveryStatus(getDeliveryStatus(deliveryRequest
                .deliveryStatusName()));
        delivery.setPaymentType(
                this.paymentTypeRepository.findByName(
                        deliveryRequest.paymentType()
                )
        );
        delivery.setCredit(deliveryRequest.credit());
        delivery.setTotal(deliveryRequest.total());
        delivery.setEvidence(deliveryRequest.evidence());
        delivery.setUser(
                this.userRepository.findUserEntityById(
                        deliveryRequest.user()
                )
        );
        delivery.setCustomer(
                this.customerRepository.findCustomerEntityById(
                        deliveryRequest.customer()
                )
        );
        delivery.setDeliveryData(deliveryRequest.deliveryData());
        delivery.setObservations(deliveryRequest.observations());
        delivery.setComments(deliveryRequest.comments());
    }

   private DeliveryStatusEntity getDeliveryStatus(String deliveryStatus) {
      return this.deliveryStatusRepository.findByName(deliveryStatus)
              .orElseGet(()-> DELIVERY_STATUS.stream().findFirst()
                      .map(r -> deliveryStatusRepository.save(
                              DeliveryStatusEntity.builder().name(r).build()))
                      .orElseThrow(() -> new EntityNotFoundException(
                              "Delivery status " + deliveryStatus + " not found"))
              );
   }
    private LocalTime calculateEstimateHourInit(DeliveryUpdateRequest deliveryRequest) {
        List<DeliveryEntity> pendingDeliveries =
                deliveryRepository.findAllByDeliveryStatusName(
                   "PENDIENTE"
                );

        // Workable Hours
        LocalTime morningStartTime = LocalTime.of(8, 0);
        LocalTime morningEndTime = LocalTime.of(12,0);
        LocalTime afternoonStartTime = LocalTime.of(13,0);
        LocalTime nightEndTime = LocalTime.of(22,0);

        LocalDate deliveryDate = deliveryRequest.deliveryDate();

        if(pendingDeliveries.isEmpty()) {
            if(now().isAfter(morningEndTime)) {
                return afternoonStartTime;
            }
            if(now().isAfter(nightEndTime)) {
                deliveryDate = deliveryDate.plusDays(1);
                return morningStartTime;
            }
            if(now().isBefore(morningStartTime)) {
                return morningStartTime;
            }
            return now();
        }


        // Filtering by the same day
        LocalDate finalDeliveryDate = deliveryDate;
        List<DeliveryEntity> deliveriesSameDay = pendingDeliveries.stream()
                .filter(d -> d.getDeliveryDate().equals(finalDeliveryDate))
                .sorted(Comparator.comparing(DeliveryEntity::getEstimateHourEnd))
                .toList();

        LocalTime lastDeliveryEnd = deliveriesSameDay.get(deliveriesSameDay
                .size() - 1).getEstimateHourEnd();

        if (lastDeliveryEnd.isAfter(nightEndTime)) {
            deliveryDate = deliveryDate.plusDays(1);
            return morningStartTime;
        }

        return lastDeliveryEnd;

    }
}
