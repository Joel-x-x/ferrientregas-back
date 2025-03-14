package com.ferrientregas.paymenttype;

import com.ferrientregas.paymenttype.exception.PaymentTypeBusinessRulesException;
import com.ferrientregas.paymenttype.exception.PaymentTypeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PaymentTypeService {

    private final PaymentTypeRepository paymentTypeRepository;

    private void createPaymentType(PaymentTypeRequest request) {
        PaymentTypeEntity paymentType =
                paymentTypeRepository.save(PaymentTypeEntity
                        .builder()
                        .name(request.name())
                        .build());

        paymentTypeRepository.save(paymentType);
    }

    private void updatePaymentType(UUID id, PaymentTypeRequest request) throws PaymentTypeNotFoundException {
        PaymentTypeEntity paymentType = paymentTypeRepository.findById(id)
                .orElseThrow(PaymentTypeNotFoundException::new);

        paymentType.setName(request.name());

        paymentTypeRepository.save(paymentType);
    }

}
