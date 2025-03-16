package com.ferrientregas.paymenttype;

import com.ferrientregas.paymenttype.dto.PaymentTypeRequest;
import com.ferrientregas.paymenttype.dto.PaymentTypeResponse;
import com.ferrientregas.paymenttype.dto.PaymentTypeUpdateRequest;
import com.ferrientregas.paymenttype.exception.PaymentTypeNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    /***
     * List payment types
     *
     * @return responses
     */
    public List<PaymentTypeResponse> list() {
        return this.
                paymentTypeRepository.findAllByDeletedFalse().stream()
                .map(x -> new PaymentTypeResponse(x.getId(), x.getName()))
                .toList();
    }

    /***
     * Get payment type
     *
     * @param id UUID
     * @return response
     * @throws PaymentTypeNotFoundException not found entity
     */
    public PaymentTypeResponse get(UUID id) throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        return new PaymentTypeResponse(
                paymentType.getId(),
                paymentType.getName()
        );
    }

    /**
     * Creates a new payment type.
     *
     * @param request the request containing the payment type details
     * @return the response containing the created payment type details
     */
    public PaymentTypeResponse create(PaymentTypeRequest request) {
        PaymentTypeEntity paymentType =
                paymentTypeRepository.save(PaymentTypeEntity
                        .builder()
                        .name(request.name())
                        .build());

        return new PaymentTypeResponse(
                paymentType.getId(),
                paymentType.getName()
        );
    }

    /***
     * Update payment type.
     *
     * @param id UUID
     * @param request payment type request
     * @return response
     * @throws PaymentTypeNotFoundException not found entity
     */
    public PaymentTypeResponse update(UUID id, PaymentTypeUpdateRequest request) throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        if(!StringUtils.isBlank(request.name()))
            paymentType.setName(request.name());

        paymentTypeRepository.save(paymentType);

        return new PaymentTypeResponse(
                paymentType.getId(),
                paymentType.getName()
        );
    }

    /***
     * Delete payment type
     *
     * @param id UUID
     * @return true Boolean
     * @throws PaymentTypeNotFoundException not found entity
     */
    public Boolean delete(UUID id) throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        paymentType.setDeleted(true);

        this.paymentTypeRepository.save(paymentType);

        return true;
    }

}
