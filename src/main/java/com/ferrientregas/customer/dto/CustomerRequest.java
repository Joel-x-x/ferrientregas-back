package com.ferrientregas.customer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CustomerRequest(
        @NotBlank
        @Size(min = 10, max = 30)
        String fullname,
        @NotBlank
        @Size(min = 9, max = 13)
        String identification,
        @NotNull
        String address,
        @NotBlank
        String addressMaps,
        @NotBlank
        @Size(min = 10)
        String phone,
        @Past
        LocalDate birthDate
) {
}
