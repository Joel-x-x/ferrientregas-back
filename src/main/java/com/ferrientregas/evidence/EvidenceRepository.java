package com.ferrientregas.evidence;

import com.ferrientregas.evidence.dto.EvidenceResponse;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EvidenceRepository extends JpaRepository<EvidenceEntity, UUID> {
    Page<EvidenceResponse> findAllByDeletedFalse(Pageable pageable);

    Page<EvidenceEntity>findAllByDeletedIsFalse(Pageable pageable);
}
