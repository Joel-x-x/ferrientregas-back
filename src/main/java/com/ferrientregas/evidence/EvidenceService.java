package com.ferrientregas.evidence;

import com.ferrientregas.delivery.DeliveryEntity;
import com.ferrientregas.delivery.DeliveryRepository;
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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvidenceService {

    private final EvidenceRepository evidenceRepository;
    private final DeliveryRepository deliveryRepository;

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

    public List<EvidenceResponse> createEvidence(EvidenceRequest request) {
       return saveAll(request)
               .stream()
               .map(EvidenceMapper::toEvidenceResponse)
               .collect(Collectors.toList());
    }

    public List<EvidenceEntity> createAll(List<String> evidence, DeliveryEntity delivery) {
        if(evidence == null || evidence.isEmpty()) return List.of();

        List<EvidenceEntity> evidenceEntities = evidence.stream().map(
                url -> EvidenceEntity.builder()
                        .url(url)
                        .delivery(delivery)
                        .build()
        ).collect(Collectors.toList());

        return this.evidenceRepository.saveAll(evidenceEntities);
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


    private List<EvidenceEntity> saveAll(EvidenceRequest request){
        DeliveryEntity delivery = deliveryRepository.findById(request.deliveryId())
                .orElseThrow(() -> new EntityNotFoundException("Delivery with id " + request.deliveryId()
                        + " not found"));

        List<EvidenceEntity> evidence = request.url().stream()
                .map(r -> EvidenceEntity.builder()
                        .url(r)
                        .delivery(delivery)
                        .build())
                .collect(Collectors.toList());

        return this.evidenceRepository.saveAll(evidence);
    }

}
