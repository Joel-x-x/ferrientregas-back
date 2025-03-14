package com.ferrientregas.deliverystatus;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Random;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "deliveries_status")
public class DeliveryStatusEntity extends Auditable {

    private String statusName;

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
