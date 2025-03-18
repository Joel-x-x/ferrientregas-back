package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerRequest;
import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.customer.dto.CustomerUpdateRequest;
import com.ferrientregas.customer.exception.CustomerNotFoundException;
import com.ferrientregas.exception.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ResultResponse<Object, String>> getAll(
            Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerService.list(pageable), 200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> get(
            @PathVariable UUID id) throws CustomerNotFoundException {
        return ResponseEntity.ok(
                ResultResponse.success(this.customerService.get(id), 200)
        );
    }

    @PostMapping
    public ResponseEntity<ResultResponse<Object, String>> create(
            @Validated @RequestBody CustomerRequest request) {
        CustomerResponse response = this.customerService.create(request);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(ResultResponse.success(response, 201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> update(
            @RequestBody
            CustomerUpdateRequest request,
            @PathVariable UUID id) throws CustomerNotFoundException {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerService.update(request, id), 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> delete(
            @PathVariable UUID id) throws CustomerNotFoundException {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerService.delete(id), 200));
    }
}
