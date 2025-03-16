package com.ferrientregas.delivery;

import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.deliverystatus.DeliveryStatusRepository;
import com.ferrientregas.paymenttype.PaymentTypeRepository;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class DeliveryService {
   private final DeliveryRepository deliveryRepository;
   private final DeliveryStatusRepository deliveryStatusRepository;
   private final PaymentTypeRepository paymentTypeRepository;
   private final UserRepository userRepository;
   private final CustomerRepository customerRepository;

    public DeliveryResponse getDelivery(UUID id) throws DeliveryNotFoundException {
        DeliveryEntity delivery = this.deliveryRepository.findById(id)
                .orElseThrow(DeliveryNotFoundException::new);
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


    public Page<DeliveryResponse> listDelivery(Pageable pageable){
        return this.deliveryRepository.findAllByDeletedFalse(pageable)
                .map(
                        delivery-> new DeliveryResponse(
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
                        ));
    }

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



   public Boolean deleteDelivery(UUID id) throws DeliveryNotFoundException {
      DeliveryEntity delivery = this.deliveryRepository.findById(id)
              .orElseThrow(DeliveryNotFoundException::new);

      delivery.setDeleted(true);
      this.deliveryRepository.save(delivery);

      return true;
   }


}
