package com.ferrientregas.customer;

import com.ferrientregas.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

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
    private String addressMaps;
    private String phone;
    private LocalDate birthDate;


}
