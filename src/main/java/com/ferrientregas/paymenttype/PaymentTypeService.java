package com.ferrientregas.paymenttype;

import com.ferrientregas.paymenttype.dto.PaymentTypeMapper;
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

    public List<PaymentTypeResponse> list() {
        return this.paymentTypeRepository.findAllByDeletedFalse().stream()
                .map(PaymentTypeMapper::toPaymentTypeResponse).toList();
    }

    public PaymentTypeResponse get(UUID id) throws PaymentTypeNotFoundException{
        return paymentTypeRepository.findById(id)
                .map(PaymentTypeMapper::toPaymentTypeResponse)
                .orElseThrow(PaymentTypeNotFoundException::new);
    }

    public PaymentTypeResponse create(PaymentTypeRequest request) {
        PaymentTypeEntity paymentType =createAndSavePaymentType(request);
        return PaymentTypeMapper.toPaymentTypeResponse(paymentType);
    }

    public PaymentTypeResponse update(UUID id, PaymentTypeUpdateRequest request)
            throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        if(!StringUtils.isBlank(request.name()))
            paymentType.setName(request.name());
        paymentTypeRepository.save(paymentType);

        return PaymentTypeMapper.toPaymentTypeResponse(paymentType);
    }

    public Boolean delete(UUID id) throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        paymentType.setDeleted(true);
        this.paymentTypeRepository.save(paymentType);

        return true;
    }


    private PaymentTypeEntity createAndSavePaymentType(
            PaymentTypeRequest paymentTypeRequest){
        return this.paymentTypeRepository.save(
                PaymentTypeEntity.builder()
                        .name(paymentTypeRequest.name())
                        .build());
    }
}
