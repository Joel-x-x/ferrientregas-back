package com.ferrientregas.customer.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CustomerRequest(
        @NotBlank
        @Size(min = 10, max = 30)
        String firstNames,
        @NotBlank
        @Size(min = 10, max = 30)
        String lastNames,
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
        LocalDate birthDate,
        // User
        @Email
        String email
) {
}
