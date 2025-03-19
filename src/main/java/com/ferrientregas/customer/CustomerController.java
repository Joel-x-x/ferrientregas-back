package com.ferrientregas.customer;

import com.ferrientregas.customer.dto.CustomerRequest;
import com.ferrientregas.customer.dto.CustomerResponse;
import com.ferrientregas.customer.dto.CustomerUpdateRequest;
import com.ferrientregas.customer.exception.CustomerNotFoundException;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResultResponse<Page<CustomerResponse>, String>> getAll(
            Pageable pageable) {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerService.list(pageable), 200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<CustomerResponse, String>> get(
            @PathVariable UUID id) {
        return ResponseEntity.ok(
                ResultResponse.success(this.customerService.get(id), 200)
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<CustomerResponse, String>> create(
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
    public ResponseEntity<ResultResponse<CustomerResponse, String>> update(
            @Valid @RequestBody CustomerUpdateRequest request,
            @PathVariable UUID id){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.customerService.update(request, id), 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
