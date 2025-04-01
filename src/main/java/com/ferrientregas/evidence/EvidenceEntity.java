package com.ferrientregas.evidence;

import com.ferrientregas.audit.Auditable;
import com.ferrientregas.delivery.DeliveryEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @ManyToOne
    @JoinColumn(name = "delivery_id", nullable = false)
    private DeliveryEntity delivery;

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
