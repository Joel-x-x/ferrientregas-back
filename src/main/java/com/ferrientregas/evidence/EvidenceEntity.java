package com.ferrientregas.evidence;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Random;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "evidences")
public class EvidenceEntity extends Auditable {
    private String url;


    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
    }

    @PreUpdate
    protected void onUpdate() {
       this.setUpdatedAt(now());
    }
}
