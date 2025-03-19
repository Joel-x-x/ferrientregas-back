package com.ferrientregas.delivery.utils;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.time.LocalTime.now;

@Component
@RequiredArgsConstructor
public class DeliveryUpdater {
    private final DeliveryStatusRepository deliveryStatusRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public void updateDeliveryFields(DeliveryEntity delivery,
                                      DeliveryUpdateRequest deliveryRequest) {

        if(!StringUtils.isBlank(deliveryRequest.numeration())) {
            delivery.setNumeration(deliveryRequest.numeration());
        }
        if(!StringUtils.isBlank(deliveryRequest.invoiceNumber())) {
            delivery.setInvoiceNumber(deliveryRequest.invoiceNumber());
        }
        if(!StringUtils.isBlank(deliveryRequest.deliveryDate())) {
            delivery.setDeliveryDate(deliveryRequest.deliveryDate());
        }
        delivery.setEstimateHourInit(now());
        delivery.setEstimateHourEnd(now());
        delivery.setDeliveryStatus(
                this.deliveryStatusRepository.findByName(
                        deliveryRequest.deliveryStatusName()
                )
        );
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
}
