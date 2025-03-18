package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.delivery.exception.DeliveryNotFoundException;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<ResultResponse<Object, String>> list(Pageable pageable) {
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.listDelivery(pageable), 200));
    }

    @GetMapping("{id}")
    public ResponseEntity<ResultResponse<Object,String>> findById(
            @PathVariable UUID id) throws DeliveryNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.getDelivery(id),200
       ));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResultResponse<Object, String>> update(
            @PathVariable UUID id,
            @RequestBody DeliveryUpdateRequest deliveryUpdateRequest
    ) throws DeliveryNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.deliveryService.updateDelivery(id,deliveryUpdateRequest),
                200
        ));
    }
    @PostMapping
    public ResponseEntity<ResultResponse<Object,String>> create(
            @RequestBody @Valid DeliveryRequest deliveryRequest
    ){
        DeliveryResponse delivery = this.deliveryService
                .createDelivery(deliveryRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(delivery.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(
                        ResultResponse.success(delivery, 201)
                );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ResultResponse<Object,String>> delete(
            @PathVariable UUID id
    ) throws DeliveryNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.deleteDelivery(id), 200
       ));
    }
}
