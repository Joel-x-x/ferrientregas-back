package com.ferrientregas.paymenttype;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Random;
import java.util.UUID;

import static java.time.LocalDateTime.now;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_types")
public class PaymentTypeEntity extends Auditable {
    private String name;

    @PrePersist
    protected void onCreate() {
        this.setCreatedAt(now());
        this.setUpdatedAt(now());
        this.setDeletedAt(now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.setCreatedAt(now());
        if(this.isDeleted()) this.setDeletedAt(now());
    }
}
