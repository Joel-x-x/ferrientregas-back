package com.ferrientregas.identificationtype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentificationTypeRepository extends JpaRepository<IdentificationTypeEntity, UUID> {
    Optional<IdentificationTypeEntity> findByTypeName(String typeName);
}
