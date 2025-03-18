package com.ferrientregas.evidence.dto;

import com.ferrientregas.evidence.EvidenceEntity;

public class EvidenceMapper {
    public static EvidenceResponse toEvidenceResponse(EvidenceEntity evidence){
        return new EvidenceResponse(
                evidence.getId(),
                evidence.getUrl()
        );
    }
}
