package com.ferrientregas.evidence.dto;

import java.util.List;
import java.util.UUID;

public record EvidenceRequest (
    List<String> url,
    UUID deliveryId
){
}
