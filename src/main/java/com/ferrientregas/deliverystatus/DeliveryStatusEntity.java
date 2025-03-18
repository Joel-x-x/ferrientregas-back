package com.ferrientregas.deliverystatus;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "delivery_status")
public class DeliveryStatusEntity extends Auditable {

    private String name;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setUpdatedAt(now());
        if(this.isDeleted()) this.setDeletedAt(now());
    }
}
