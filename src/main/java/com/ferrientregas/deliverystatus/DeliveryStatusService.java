package com.ferrientregas.deliverystatus;

import com.ferrientregas.deliverystatus.dto.DeliveryStatusMapper;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusRequest;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusResponse;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusUpdateRequest;
import com.ferrientregas.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.ferrientregas.paymenttype.dto.PaymentTypeRequest;
import com.ferrientregas.paymenttype.dto.PaymentTypeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryStatusService {

    private final DeliveryStatusRepository deliveryStatusRepository;

    public List<DeliveryStatusResponse> list() {
        return this.deliveryStatusRepository.findAllByDeletedFalse().stream()
                .map(DeliveryStatusMapper::toDeliveryStatusResponse).toList();
    }

    public DeliveryStatusResponse get(UUID id) throws DeliveryStatusNotFoundException {
        return this.deliveryStatusRepository.findById(id)
                .map(DeliveryStatusMapper::toDeliveryStatusResponse)
                .orElseThrow(DeliveryStatusNotFoundException::new);
    }

    public DeliveryStatusResponse create(DeliveryStatusRequest request) {
        DeliveryStatusEntity deliveryStatus = createAndSaveDeliveryStatus(request);
        return DeliveryStatusMapper.toDeliveryStatusResponse(deliveryStatus);
    }

    public DeliveryStatusResponse update(DeliveryStatusUpdateRequest request,
     UUID id) throws DeliveryStatusNotFoundException {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .findById(id)
                .orElseThrow(DeliveryStatusNotFoundException::new);

        deliveryStatus.setName(request.name());
        this.deliveryStatusRepository.save(deliveryStatus);
        return DeliveryStatusMapper.toDeliveryStatusResponse(deliveryStatus);
    }

    public Boolean delete(UUID id) throws DeliveryStatusNotFoundException {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .findById(id)
                .orElseThrow(DeliveryStatusNotFoundException::new);
        deliveryStatus.setDeleted(true);
        this.deliveryStatusRepository.save(deliveryStatus);

        return true;
    }


    private DeliveryStatusEntity createAndSaveDeliveryStatus(
            DeliveryStatusRequest request) {
        return this.deliveryStatusRepository.save(DeliveryStatusEntity.builder()
                .name(request.name())
                .build());
    }
}
