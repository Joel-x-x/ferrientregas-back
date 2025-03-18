package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceRequest;
import com.ferrientregas.evidence.dto.EvidenceResponse;
import com.ferrientregas.evidence.dto.EvidenceUpdateRequest;
import com.ferrientregas.evidence.exception.EvidenceNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;

    public EvidenceResponse getEvidenceById(UUID id)
            throws EvidenceNotFoundException {
        EvidenceEntity evidence = evidenceRepository.findById(id)
                .orElseThrow(EvidenceNotFoundException::new);
        return new EvidenceResponse(
                evidence.getId(),
                evidence.getUrl()
        );
    }

    public Page<EvidenceResponse> getEvidences(Pageable pageable) {
        return this.evidenceRepository.findAllByDeletedFalse(pageable)
                .map(evidence-> new EvidenceResponse(
                  evidence.id(),
                  evidence.url()
                ));
    }

    public EvidenceResponse createEvidence(EvidenceRequest evidenceRequest) {
       EvidenceEntity evidence = evidenceRepository.save(EvidenceEntity
                       .builder()
                       .url(evidenceRequest.url())
               .build()
       );

       return new EvidenceResponse(
               evidence.getId(),
               evidence.getUrl()
       );
    }

    public EvidenceResponse updateEvidence
            (UUID id, EvidenceUpdateRequest evidenceUpdateRequest) throws EvidenceNotFoundException {
       EvidenceEntity evidence = evidenceRepository.findById(id)
               .orElseThrow(EvidenceNotFoundException::new);

       if(!StringUtils.isBlank(evidence.getUrl())) {
          evidence.setUrl(evidenceUpdateRequest.url());
       }
       evidenceRepository.save(evidence);

        return new EvidenceResponse(
                evidence.getId(),
                evidence.getUrl()
        );
    }

    public Boolean deleteEvidence(UUID id) throws EvidenceNotFoundException {
       EvidenceEntity evidence = evidenceRepository.findById(id)
               .orElseThrow(EvidenceNotFoundException::new);
       evidence.setDeleted(true);
       evidenceRepository.save(evidence);
       return true;
    }

}
