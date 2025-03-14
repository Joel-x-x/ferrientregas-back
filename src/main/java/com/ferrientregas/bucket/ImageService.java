package com.ferrientregas.bucket;

import com.ferrientregas.bucket.dto.CreateImageDto;
import com.ferrientregas.bucket.dto.UpdateImageDto;
import com.ferrientregas.exception.IntegrityValidation;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
public class ImageService {

    private final Storage storage;
    private final String bucketName;

    public ImageService(Storage storage, @Value("${gcp.bucket.name}") String bucketName) {
        this.storage = storage;
        this.bucketName = bucketName;
    }
    /**
     * Subir una imagen al bucket.
     */
    public String uploadImage(CreateImageDto createImageDto) throws IOException {
        try {
            // Validar que la imagen no sea nula o esté vacía
            if (createImageDto.file() == null || createImageDto.file().isEmpty()) {
                throw new IntegrityValidation("La imagen no puede estar vacía.");
            }

            // Validar extensión de la imagen
            String originalFileName = Objects.requireNonNull(createImageDto.file()
                    .getOriginalFilename());
            if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
                throw new IntegrityValidation("La imagen debe tener extensión .jpg o .png.");
            }

            // Validar el tamaño de la imagen
            if (createImageDto.file().getSize() > 5 * 1024 * 1024) { // 5 MB en bytes
                throw new IntegrityValidation("La imagen excede el tamaño máximo permitido de 5 MB.");
            }

            // Generar un nombre único para la imagen
            String fileName = generateUniqueFileName(originalFileName);

            // Verificar si el bucket existe
            Bucket bucket = storage.get(bucketName);
            if (bucket == null) {
                throw new IntegrityValidation("El bucket no existe: " + bucketName);
            }

            // Subir la imagen al bucket
            Blob blob = bucket
                    .create(createImageDto.folderName() + "/" + fileName,
                            createImageDto.file().getBytes(), createImageDto
                                    .file().getContentType());

            // Retornar el enlace público de la imagen
            return "https://storage.googleapis.com/" + bucketName + "/" +
                    createImageDto.folderName() + "/" + fileName;
        } catch (Exception e) {
            throw new IntegrityValidation("Ocurrió un error al subir la imagen: "
                    + e.getMessage());
        }
    }

    /**
     * Actualizar una imagen en el bucket.
     */
    public String updateImage(UpdateImageDto updateImageDto) throws IOException {
        try {
            // Validar que el archivo no sea nulo o esté vacío
            if (updateImageDto.file() == null || updateImageDto.file().isEmpty()) {
                throw new IntegrityValidation("El archivo nuevo no puede estar vacío.");
            }

            // Validar extensión del archivo
            String originalFileName = Objects.requireNonNull(updateImageDto.file()
                    .getOriginalFilename());
            if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
                throw new IntegrityValidation("El archivo debe ser una imagen con extensión .jpg o .png.");
            }

            // Eliminar la imagen anterior
            deleteImage(updateImageDto.folderName(), updateImageDto.oldFileName());

            // Subir la nueva imagen
            return uploadImage(new CreateImageDto(updateImageDto.file(),
                    updateImageDto.folderName()));
        } catch (Exception e) {
            throw new IntegrityValidation("Ocurrió un error al actualizar la imagen: "
                    + e.getMessage());
        }
    }

    /**
     * Eliminar una imagen del bucket.
     */
    public void deleteImage(String folderName, String fileName) {
        // Filename with extension
        try {
            String filePath = folderName + "/" + fileName;
            Blob blob = storage.get(bucketName, filePath);

            if (blob != null && blob.exists()) {
                boolean deleted = blob.delete();
                if (!deleted) {
                    throw new IntegrityValidation("No se puedo eliminar la imagen.");
                }
            } else {
                throw new IntegrityValidation("La imagen no existe o ya fue eliminada.");
            }
        } catch (Exception e) {
            throw new IntegrityValidation("Ocurrio un error al intentar eliminar la imagen.");
        }
    }

    /**
     * Generar un nombre único para la imagen.
     */
    private String generateUniqueFileName(String originalFileName) {
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID() + fileExtension;
    }
}

@Builder
class Response {
    public Boolean status;
    public String message;
}
