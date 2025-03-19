package com.ferrientregas.paymenttype;

import com.ferrientregas.paymenttype.dto.PaymentTypeMapper;
import com.ferrientregas.paymenttype.dto.PaymentTypeRequest;
import com.ferrientregas.paymenttype.dto.PaymentTypeResponse;
import com.ferrientregas.paymenttype.dto.PaymentTypeUpdateRequest;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
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

    public PaymentTypeResponse get(UUID id){
        return paymentTypeRepository.findById(id)
                .map(PaymentTypeMapper::toPaymentTypeResponse)
                .orElseThrow(()->
                        new EntityNotFoundException("PaymentType with id " + id
                                + " not found"));
    }

    public PaymentTypeResponse create(PaymentTypeRequest request) {
        PaymentTypeEntity paymentType =createAndSavePaymentType(request);
        return PaymentTypeMapper.toPaymentTypeResponse(paymentType);
    }

    public PaymentTypeResponse update(UUID id, PaymentTypeUpdateRequest request){
        PaymentTypeEntity paymentType = getPaymentTypeById(id);

        if(!StringUtils.isBlank(request.name()))
            paymentType.setName(request.name());
        paymentTypeRepository.save(paymentType);

        return PaymentTypeMapper.toPaymentTypeResponse(paymentType);
    }

    public Boolean delete(UUID id){
        PaymentTypeEntity paymentType = getPaymentTypeById(id);

        paymentType.setDeleted(true);
        this.paymentTypeRepository.save(paymentType);

        return true;
    }

    private PaymentTypeEntity getPaymentTypeById(UUID id){
       return this.paymentTypeRepository.findById(id)
               .orElseThrow(()->
                       new EntityNotFoundException("PaymentType with id " + id
                               + " not found"));
    }

    private PaymentTypeEntity createAndSavePaymentType(
            PaymentTypeRequest paymentTypeRequest){
        return this.paymentTypeRepository.save(
                PaymentTypeEntity.builder()
                        .name(paymentTypeRequest.name())
                        .build());
    }
}
