package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceRequest;
import com.ferrientregas.evidence.dto.EvidenceResponse;
import com.ferrientregas.evidence.dto.EvidenceUpdateRequest;
import com.ferrientregas.evidence.exception.EvidenceNotFoundException;
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
@RequestMapping("/api/v1/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @GetMapping
    public ResponseEntity<ResultResponse<Object,String>> list(
            Pageable pageable
    ){
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.getEvidences(pageable), 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> getEvidence(
            @PathVariable UUID id
    ) throws EvidenceNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.getEvidenceById(id), 200));
    }

    @PostMapping
    public ResponseEntity<ResultResponse<Object,String>> createEvidence(
            @Validated @RequestBody EvidenceRequest request
    ){
        EvidenceResponse evidence = this.evidenceService.createEvidence(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(evidence.id())
                .toUri();

        return ResponseEntity.created(location)
                .body(ResultResponse.success(evidence,201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> updateEvidence(
            @PathVariable UUID id,
            @Validated @RequestBody EvidenceUpdateRequest request
    ) throws EvidenceNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.updateEvidence(id,request),200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> deleteEvidence(
            @PathVariable UUID id
    ) throws EvidenceNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.evidenceService.deleteEvidence(id), 200));
    }
}
