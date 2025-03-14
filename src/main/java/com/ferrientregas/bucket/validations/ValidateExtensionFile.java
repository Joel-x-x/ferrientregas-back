package com.ferrientregas.bucket.validations;

import com.ferrientregas.exception.IntegrityValidation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ValidateExtensionFile implements ImageValidations {

    @Override
    public void validate(MultipartFile file) {
        // Validate extension file .jpg or .png
        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
            throw new IntegrityValidation("El archivo debe ser una imagen con extensión .jpg o .png.");
        }
    }
}
