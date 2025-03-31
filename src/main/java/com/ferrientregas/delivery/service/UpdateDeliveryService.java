package com.ferrientregas.delivery.service;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.delivery.rules.DeliveryBusinessLogicValidations;
import com.ferrientregas.deliverystatus.DeliveryStatusEntity;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class UpdateDeliveryService {
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final List<DeliveryBusinessLogicValidations> deliveryBusinessLogicValidations;

    public void update(DeliveryEntity delivery,
                                      DeliveryUpdateRequest request) {

        if(!StringUtils.isBlank(request.invoiceNumber())) {
            delivery.setInvoiceNumber(request.invoiceNumber());
        }
        if(request.deliveryDate() != null) {
            delivery.setDeliveryDate(request.deliveryDate());
        }
        if(request.estimateHourInit() != null) {
            delivery.setEstimateHourInit(calculateEstimateHourInit(request));
        }
        if(request.estimateHourEnd() != null) {
            delivery.setEstimateHourEnd(request.estimateHourInit().plusHours(1));
        }
//        if(request.deliveryStatus() != null) {
//            delivery.setDeliveryStatus(getDeliveryStatus(request
//                    .deliveryStatus()));
//        }
        if(request.paymentType() != null) {
//            delivery.setPaymentType(
//                    this.paymentTypeRepository.findByName(
//                            request.paymentType()
//                    )
//            );
        }
        if(request.credit() != null) {
            delivery.setCredit(request.credit());
        }
        if(request.total() != null) {
            delivery.setTotal(request.total());
        }
        delivery.setEvidence(request.evidence());
        delivery.setUser(
                this.userRepository.findUserEntityById(
                        request.user()
                )
        );
        delivery.setCustomer(
                this.customerRepository.findCustomerEntityById(
                        request.customer()
                )
        );
        delivery.setDeliveryData(request.deliveryData());
        delivery.setObservations(request.observations());
        delivery.setComments(request.comments());
    }

//   private DeliveryStatusEntity getDeliveryStatus(String deliveryStatus) {
//      return this.deliveryStatusRepository.findByName(deliveryStatus)
//              .orElseGet(()-> DELIVERY_STATUS.stream().findFirst()
//                      .map(r -> deliveryStatusRepository.save(
//                              DeliveryStatusEntity.builder().name(r).build()))
//                      .orElseThrow(() -> new EntityNotFoundException(
//                              "Delivery status " + deliveryStatus + " not found"))
//              );
//   }
    private LocalTime calculateEstimateHourInit(DeliveryUpdateRequest request) {
        List<DeliveryEntity> pendingDeliveries =
                deliveryRepository.findAllByDeliveryStatusName(
                   "PENDIENTE"
                );
        deliveryBusinessLogicValidations.forEach(
                validation -> {
                   validation.validateInitHour(request.deliveryDate(),
                           pendingDeliveries);
                }
        );

        return now();
    }
}
