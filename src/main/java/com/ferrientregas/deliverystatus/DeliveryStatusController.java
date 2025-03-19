package com.ferrientregas.deliverystatus;

import com.ferrientregas.deliverystatus.dto.DeliveryStatusRequest;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusResponse;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusUpdateRequest;
import com.ferrientregas.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/delivery-status")
@RequiredArgsConstructor
public class DeliveryStatusController {

    private final DeliveryStatusService deliveryStatusService;

    @GetMapping
    public ResponseEntity<ResultResponse<Object, String>> list() {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.list(),
                        200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> get(
            @PathVariable UUID id){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.get(id), 200
                )
        );
    }

    @PostMapping
    public ResponseEntity<ResultResponse<Object, String>> create(
            @Valid @RequestBody DeliveryStatusRequest request) {
        DeliveryStatusResponse response = this.deliveryStatusService
                .create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(ResultResponse.success(
                        response, 201
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> update(
            @Valid @RequestBody DeliveryStatusUpdateRequest request,
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.update(
                                request,
                                id
                        ),
                        200
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Object, String>> delete(
            @PathVariable UUID id) throws DeliveryStatusNotFoundException {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.delete(id),
                        200
                )
        );
    }
}
