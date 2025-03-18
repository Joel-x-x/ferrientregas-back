package com.ferrientregas.customer.dto;

import java.time.LocalDate;

public record CustomerUpdateRequest(
        String firstNames,
        String lastNames,
        String identification,
        String address,
        String addressMaps,
        String phone,
        LocalDate birthDate
) {
}
