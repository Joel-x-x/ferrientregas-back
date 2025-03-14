package com.ferrientregas.bucket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateImageDto(
        @NotNull(message = "El archivo no puede ser nulo.")
        MultipartFile file,
        @NotBlank
        String folderName,
        @NotBlank
        String oldFileName
) {
}
