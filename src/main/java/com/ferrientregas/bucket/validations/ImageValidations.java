package com.ferrientregas.bucket.validations;

import org.springframework.web.multipart.MultipartFile;

public interface ImageValidations {
    public void validate(MultipartFile file);
}
