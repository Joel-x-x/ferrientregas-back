package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.*;
import com.ferrientregas.delivery.utils.DeliveryFactory;
import com.ferrientregas.delivery.utils.DeliveryMapper;
import com.ferrientregas.delivery.utils.DeliveryUpdater;
import jakarta.persistence.EntityNotFoundException;
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
   private final DeliveryFactory deliveryFactory;
   private final DeliveryUpdater deliveryUpdater;

    public DeliveryResponse getDelivery(UUID id) {
        return deliveryRepository.findById(id)
                .map(DeliveryMapper::toDeliveryResponse)
                .orElseThrow(
                        ()-> new EntityNotFoundException("No delivery found for id:"
                                + id));
    }

    public Page<DeliveryResponse> listDelivery(Pageable pageable){
        return deliveryRepository.findAllByDeletedIsFalse(pageable)
                .map(DeliveryMapper::toDeliveryResponse);
    }

   public DeliveryResponse createDelivery(DeliveryRequest deliveryRequest) {
       DeliveryEntity delivery = deliveryFactory.createDelivery(deliveryRequest);
       deliveryRepository.save(delivery);
       return DeliveryMapper.toDeliveryResponse(delivery);
   }

    public DeliveryResponse updateDelivery(UUID id,
            DeliveryUpdateRequest deliveryRequest
    ){
       DeliveryEntity delivery = getDeliveryById(id);
        deliveryUpdater.updateDeliveryFields(delivery, deliveryRequest);
        deliveryRepository.save(delivery);
        return DeliveryMapper.toDeliveryResponse(delivery);
    }

   public void deleteDelivery(UUID id) {
      DeliveryEntity delivery = getDeliveryById(id);
      delivery.setDeleted(true);
      deliveryRepository.save(delivery);
   }

   private DeliveryEntity getDeliveryById(UUID id){
        return deliveryRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("No delivery " +
                        "found for id: " + id));
   }
}
