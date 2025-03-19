package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceRequest;
import com.ferrientregas.evidence.dto.EvidenceResponse;
import com.ferrientregas.evidence.dto.EvidenceUpdateRequest;
import com.ferrientregas.exception.ResultResponse;
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
@RequestMapping("/api/v1/evidences")
@RequiredArgsConstructor
public class EvidenceController {

    private final EvidenceService evidenceService;

    @GetMapping
    public ResponseEntity<ResultResponse<Page<EvidenceResponse>,String>> list(
            Pageable pageable
    ){
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.getEvidences(pageable), 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<EvidenceResponse,String>> getEvidence(
            @PathVariable UUID id
    ){
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.getEvidence(id), 200));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<EvidenceResponse,String>> createEvidence(
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
    public ResponseEntity<ResultResponse<EvidenceResponse,String>> updateEvidence(
            @PathVariable UUID id,
            @Validated @RequestBody EvidenceUpdateRequest request
    ){
       return ResponseEntity.ok(ResultResponse.success(
               this.evidenceService.updateEvidence(id,request),200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.evidenceService.deleteEvidence(id);
        return ResponseEntity.noContent().build();
    }
}
