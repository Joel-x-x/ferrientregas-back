package com.ferrientregas.evidence.dto;

import java.util.UUID;

public record EvidenceResponse (
        UUID id,
        String url
){
}
