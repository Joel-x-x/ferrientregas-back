package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;
   private final DeliveryStatusRepository deliveryStatusRepository;
   private final PaymentTypeRepository paymentTypeRepository;
   private final UserRepository userRepository;
   private final CustomerRepository customerRepository;

   public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest) {
       DeliveryEntity delivery =
               this.deliveryRepository.save(DeliveryEntity.builder()
                       .numeration(deliveryRequest.numeration())
                       .invoiceNumber(deliveryRequest.invoiceNumber())
                       .deliveryDate(deliveryRequest.DeliveryDate())
                       .estimateHourInit(now())
                       .estimateHourEnd(null)
                       .deliveryStatus(
                               deliveryStatusRepository.findByName(
                                       deliveryRequest.deliveryStatusName()
                               )
                       )
                       .paymentType(
                               paymentTypeRepository.findByName(
                                       deliveryRequest.paymentType()
                               )
                       )
                       .credit(deliveryRequest.credit())
                       .total(deliveryRequest.total())
                       .evidence(deliveryRequest.evidence())
                       .user(
                               userRepository.findUserEntityById(
                                       deliveryRequest.user()
                               )
                       )
                       .customer(
                               customerRepository.findCustomerEntityById(
                                       deliveryRequest.customer()
                               )
                       )
                       .deliveryData(deliveryRequest.deliveryData())
                       .observations(deliveryRequest.observations())
                       .comments(deliveryRequest.comments())
                       .build()
               );
       return new DeliveryResponse(
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
