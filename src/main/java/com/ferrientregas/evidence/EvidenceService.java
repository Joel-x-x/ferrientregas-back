package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceMapper;
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
        return this.evidenceRepository.findById(id)
                .map(EvidenceMapper::toEvidenceResponse)
                .orElseThrow(EvidenceNotFoundException::new);
    }

    public Page<EvidenceResponse> getEvidences(Pageable pageable) {
        return this.evidenceRepository.findAllByDeletedIsFalse(pageable)
                .map(EvidenceMapper::toEvidenceResponse);
    }

    public EvidenceResponse createEvidence(EvidenceRequest evidenceRequest) {
       EvidenceEntity evidence = createAndSaveEvidence(evidenceRequest);
       return EvidenceMapper.toEvidenceResponse(evidence);
    }

    public EvidenceResponse updateEvidence(
                    UUID id, EvidenceUpdateRequest evidenceUpdateRequest
    ) throws EvidenceNotFoundException {
       EvidenceEntity evidence = evidenceRepository.findById(id)
               .orElseThrow(EvidenceNotFoundException::new);
       if(!StringUtils.isBlank(evidence.getUrl())) {
          evidence.setUrl(evidenceUpdateRequest.url());
       }
       evidenceRepository.save(evidence);

       return EvidenceMapper.toEvidenceResponse(evidence);
    }

    public Boolean deleteEvidence(UUID id) throws EvidenceNotFoundException {
       EvidenceEntity evidence = evidenceRepository.findById(id)
               .orElseThrow(EvidenceNotFoundException::new);
       evidence.setDeleted(true);
       evidenceRepository.save(evidence);
       return true;
    }


    private EvidenceEntity createAndSaveEvidence(EvidenceRequest evidenceRequest){
        return this.evidenceRepository.save(EvidenceEntity.builder()
                .url(evidenceRequest.url())
                .build()
        );
    }

}
