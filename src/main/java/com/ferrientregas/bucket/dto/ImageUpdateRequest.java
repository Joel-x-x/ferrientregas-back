package com.ferrientregas.bucket.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ImageUpdateRequest(
        @NotNull(message = "Images can be null.")
        List<MultipartFile> files,
        @NotBlank
        String folderName,
        @NotBlank
        List<String> oldFileNames
) {
}
