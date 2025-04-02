package com.ferrientregas.bucket;

import com.ferrientregas.bucket.dto.ImageRequest;
import com.ferrientregas.bucket.dto.ImageUpdateRequest;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ResultResponse<Object,String>> uploadImages(
            @ModelAttribute @Valid ImageRequest request) throws IOException {
        List<String> urls = imageService.uploadImages(request);
        return ResponseEntity.ok(ResultResponse
                .success(urls,200));
    }

    @PutMapping("/update")
    public ResponseEntity<ResultResponse<Object,String>> updateImages(
            @ModelAttribute @Valid ImageUpdateRequest imageUpdateRequest) throws IOException {
        List<String> imageUrl = imageService.updateImages(imageUpdateRequest);
        return ResponseEntity.ok(ResultResponse
                .success(imageUrl,200));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<Object,String>> deleteImages(
            @RequestParam("folderName") String folderName,
            @RequestParam("fileName") String fileName) {
        imageService.deleteImage(folderName, fileName);
        return ResponseEntity.ok(ResultResponse
                .success("Imagen eliminada correctamente",200));
    }
}
