package com.ferrientregas.bucket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ImageRequest(
        @NotNull(message = "Images can be null.")
        List<MultipartFile> files,
        @NotBlank
        @Size(max = 100, message = "El nombre de la carpeta no puede exceder los 100 caracteres.")
        String folderName
) {
}
