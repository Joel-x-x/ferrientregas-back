package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.exception.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<ResultResponse<Object, String>> paginate(Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerRepository.findAllByDeletedIsFalse(pageable)
                                .map(x -> new CustomerResponse(
                                        x.getId(),
                                        x.getFullName(),
                                        x.getIdentification(),
                                        x.getAddress(),
                                        x.getAddressMaps(),
                                        x.getPhone(),
                                        x.getBirthDate()
                                )), 200
                )
        );
    }
}
