package com.ferrientregas.deliverystatus;

import com.ferrientregas.deliverystatus.dto.DeliveryStatusRequest;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusResponse;
import com.ferrientregas.deliverystatus.dto.DeliveryStatusUpdateRequest;
import com.ferrientregas.deliverystatus.exception.DeliveryStatusNotFoundException;
import com.ferrientregas.exception.ResultResponse;
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
@RequestMapping("api/v1/delivery-status")
@RequiredArgsConstructor
public class DeliveryStatusController {

    private final DeliveryStatusService deliveryStatusService;

    @GetMapping
    public ResponseEntity<ResultResponse<List<DeliveryStatusResponse>,
            String>> list() {
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.list(),
                        200
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<DeliveryStatusResponse, String>> get(
            @PathVariable UUID id){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.get(id), 200
                )
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<DeliveryStatusResponse, String>> create(
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
    public ResponseEntity<ResultResponse<DeliveryStatusResponse, String>> update(
            @Valid @RequestBody DeliveryStatusUpdateRequest request,
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(
                ResultResponse.success(
                        this.deliveryStatusService.update(request, id),
                        200
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.deliveryStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
