package com.ferrientregas.deliverystatus;

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

    /***
     * List delivery status
     *
     * @return responses
     */
    public List<DeliveryStatusResponse> list() {
        return this.deliveryStatusRepository.findAllByDeletedFalse().stream()
                .map(x -> new DeliveryStatusResponse(
                        x.getId(),
                        x.getName()
                )).toList();
    }

    /***
     * Get delivery status
     *
     * @param id UUID
     * @return response
     * @throws DeliveryStatusNotFoundException not found exception
     */
    public DeliveryStatusResponse get(UUID id) throws DeliveryStatusNotFoundException {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .findById(id)
                .orElseThrow(DeliveryStatusNotFoundException::new);

        return new DeliveryStatusResponse(
                deliveryStatus.getId(),
                deliveryStatus.getName());
    }

    /***
     * Create delivery status
     *
     * @param request delivery status
     * @return response
     */
    public DeliveryStatusResponse create(DeliveryStatusRequest request) {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .save(DeliveryStatusEntity.builder()
                        .name(request.name())
                        .build());

        return new DeliveryStatusResponse(
                deliveryStatus.getId(),
                deliveryStatus.getName()
        );
    }

    /***
     * Update delivery status
     *
     * @param request delivery status
     * @param id UUID
     * @return response
     * @throws DeliveryStatusNotFoundException not found exception
     */
    public DeliveryStatusResponse update(DeliveryStatusUpdateRequest request,
     UUID id) throws DeliveryStatusNotFoundException {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .findById(id)
                .orElseThrow(DeliveryStatusNotFoundException::new);

        deliveryStatus.setName(request.name());
        this.deliveryStatusRepository.save(deliveryStatus);

        return new DeliveryStatusResponse(
                deliveryStatus.getId(),
                deliveryStatus.getName()
        );
    }

    /***
     * Delete delivery status
     *
     * @param id UUID
     * @return true Boolean
     * @throws DeliveryStatusNotFoundException not found exception
     */
    public Boolean delete(UUID id) throws DeliveryStatusNotFoundException {
        DeliveryStatusEntity deliveryStatus = this.deliveryStatusRepository
                .findById(id)
                .orElseThrow(DeliveryStatusNotFoundException::new);

        deliveryStatus.setDeleted(true);

        return true;
    }
}
