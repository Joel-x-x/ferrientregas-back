package com.ferrientregas.report;

import com.ferrientregas.exception.ResultResponse;
import com.ferrientregas.report.dto.DeliveryCountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/users/count/by-role/{role}")
    public ResponseEntity<ResultResponse<Long, String>> countUsersByRoleName(
            @PathVariable String role
    ) {
        return ResponseEntity.ok(
                ResultResponse.success(this.reportService.countUsersByRole(role), 200)
        );
    }

    @PostMapping("/deliveries/count")
    public ResponseEntity<ResultResponse<Long, String>> countDeliveries(
            @RequestBody(required = false) DeliveryCountRequest request
            ) {
        return ResponseEntity.ok(
                ResultResponse.success(this.reportService.countDeliveries(request), 200)
        );
    }
}
