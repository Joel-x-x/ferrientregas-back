package com.ferrientregas.identificationtype;

import com.ferrientregas.audit.Auditable;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "identification_types")
public class IdentificationTypeEntity extends Auditable {
    private String typeName;
}
