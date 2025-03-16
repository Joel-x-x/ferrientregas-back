package com.ferrientregas.delivery;

import com.ferrientregas.delivery.dto.DeliveryRequest;
import com.ferrientregas.exception.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public ResponseEntity<ResultResponse<Object,String>> create(
            @RequestBody DeliveryRequest deliveryRequest
    ){
       return ResponseEntity.ok(ResultResponse.success(deliveryService
                       .createDelivery(deliveryRequest), 200));
    }
}
