package com.ferrientregas.customer;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.ferrientregas.audit.Auditable;
import com.ferrientregas.identificationtype.IdentificationTypeEntity;
import com.ferrientregas.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerEntity extends UserEntity {
    private String fullName;
    private String identification;
    private String address;
    private LocalDate birthDate;
//    @OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
//    @JoinColumn(name = "user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
//    @JsonIdentityReference(alwaysAsId = true)
//    @JsonProperty("user_id")
//    private UserEntity user;
}
