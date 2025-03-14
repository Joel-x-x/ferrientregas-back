package com.ferrientregas.bucket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record CreateImageDto(
        @NotNull(message = "El archivo no puede ser nulo.")
        MultipartFile file,
        @NotBlank
        @Size(max = 100, message = "El nombre de la carpeta no puede exceder los 100 caracteres.")
        String folderName
) {
}
