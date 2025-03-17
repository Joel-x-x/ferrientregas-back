package com.ferrientregas.customer.dto;

import java.time.LocalDate;
import java.util.UUID;

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
