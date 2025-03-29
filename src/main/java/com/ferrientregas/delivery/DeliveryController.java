package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.delivery.dto.DeliveryResponse;
import com.ferrientregas.delivery.dto.DeliveryUpdateRequest;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<ResultResponse<Page<DeliveryResponse>, String>> list(Pageable pageable) {
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.listDelivery(pageable), 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<DeliveryResponse,String>> findById(
            @PathVariable UUID id) {
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.getDelivery(id),200
       ));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ResultResponse<Page<DeliveryResponse>,String>> findByUserId(
            @PathVariable UUID userId,
            Pageable pageable
    ){
       return ResponseEntity.ok(ResultResponse.success(
               this.deliveryService.getDeliveryByUserId(pageable, userId), 200
       ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<DeliveryResponse, String>> update(
            @PathVariable UUID id,
            @RequestBody DeliveryUpdateRequest deliveryUpdateRequest
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.deliveryService.updateDelivery(id,deliveryUpdateRequest),
                200
        ));
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<DeliveryResponse,String>> create(
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

    @GetMapping("/next-available-slot")
    public ResponseEntity<ResultResponse<Optional<Map<String,LocalTime>>,
            String>>
    nextAvailableSlot(){
        return ResponseEntity.ok(ResultResponse.success(
                this.deliveryService.getNextAvailableHours(), 200
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.deliveryService.deleteDelivery(id);
        return ResponseEntity.noContent().build();
    }
}
