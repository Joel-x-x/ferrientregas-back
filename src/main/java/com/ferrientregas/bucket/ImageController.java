package com.ferrientregas.bucket;

import com.ferrientregas.bucket.dto.CreateImageDto;
import com.ferrientregas.bucket.dto.UpdateImageDto;
import com.ferrientregas.exception.ResultResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/image")
public class ImageController {

    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ResultResponse<Object,String>> uploadImage(
            @ModelAttribute @Valid CreateImageDto createImageDto) throws IOException {
        String imageUrl = imageService.uploadImage(createImageDto);
        return ResponseEntity.ok(ResultResponse
                .success(imageUrl,200));
    }

    @PutMapping("/upload")
    public ResponseEntity<ResultResponse<Object,String>> updateImage(
            @ModelAttribute @Valid UpdateImageDto updateImageDto) throws IOException {
        String imageUrl = imageService.updateImage(updateImageDto);
        return ResponseEntity.ok(ResultResponse
                .success(imageUrl,200));
    }


    @DeleteMapping("/delete")
    public ResponseEntity<ResultResponse<Object,String>> deleteImage(
            @RequestParam("folderName") String folderName,
            @RequestParam("fileName") String fileName) {
        imageService.deleteImage(folderName, fileName);
        return ResponseEntity.ok(ResultResponse
                .success("Imagen eliminada correctamente",200));
    }
}
