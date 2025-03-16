package com.ferrientregas.delivery;

import com.ferrientregas.exception.ResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ResultResponse<Object,String>> create(
            @RequestBody DeliveryRequest deliveryRequest
    ){
       return ResponseEntity.ok(ResultResponse.success(deliveryService
                       .createDelivery(deliveryRequest), 200));
    }
}
