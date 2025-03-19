package com.ferrientregas.paymenttype;

import com.ferrientregas.exception.ResultResponse;
import com.ferrientregas.paymenttype.dto.PaymentTypeRequest;
import com.ferrientregas.paymenttype.dto.PaymentTypeResponse;
import com.ferrientregas.paymenttype.dto.PaymentTypeUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment-types")
@RequiredArgsConstructor
public class PaymentTypeController {

    private final PaymentTypeService paymentTypeService;

    @GetMapping
    public ResponseEntity<ResultResponse<List<PaymentTypeResponse>, String>> list() {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.paymentTypeService.list(), 200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<PaymentTypeResponse, String>> get(
            @PathVariable UUID id){
        return ResponseEntity.ok(
                ResultResponse.success(this.paymentTypeService.get(id), 200));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<PaymentTypeResponse, String>> create(
            @Valid @RequestBody PaymentTypeRequest request) {
        PaymentTypeResponse response = this.paymentTypeService.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(ResultResponse.success(response, 201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<PaymentTypeResponse, String>> update(
            @PathVariable UUID id,
            @RequestBody PaymentTypeUpdateRequest request){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.paymentTypeService.update(id, request), 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.paymentTypeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
