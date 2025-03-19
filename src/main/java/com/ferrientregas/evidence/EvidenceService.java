package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceMapper;
import com.ferrientregas.evidence.dto.EvidenceRequest;
import com.ferrientregas.evidence.dto.EvidenceResponse;
import com.ferrientregas.evidence.dto.EvidenceUpdateRequest;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;

    public EvidenceResponse getEvidence(UUID id)
             {
        return this.evidenceRepository.findById(id)
                .map(EvidenceMapper::toEvidenceResponse)
                .orElseThrow(()->
                        new EntityNotFoundException("Evidence with id "
                                + id + " not found"));
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
    ){
       EvidenceEntity evidence = getEvidenceById(id);
       if(!StringUtils.isBlank(evidence.getUrl())) {
          evidence.setUrl(evidenceUpdateRequest.url());
       }
       evidenceRepository.save(evidence);

       return EvidenceMapper.toEvidenceResponse(evidence);
    }

    public Boolean deleteEvidence(UUID id) {
       EvidenceEntity evidence = getEvidenceById(id);
       evidence.setDeleted(true);
       evidenceRepository.save(evidence);
       return true;
    }

    private EvidenceEntity getEvidenceById(UUID id){
       return this.evidenceRepository.findById(id)
               .orElseThrow(()->
                       new EntityNotFoundException("Evidence with id "
                               + id + " not found"));
    }


    private EvidenceEntity createAndSaveEvidence(EvidenceRequest evidenceRequest){
        return this.evidenceRepository.save(EvidenceEntity.builder()
                .url(evidenceRequest.url())
                .build()
        );
    }

}
