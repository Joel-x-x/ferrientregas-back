package com.ferrientregas.role;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
@Table(name="roles")
public class RoleEntity extends Auditable {
    @NotNull
    @Column(nullable = false, unique = true)
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
    }
}
