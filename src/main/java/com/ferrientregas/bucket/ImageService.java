package com.ferrientregas.bucket;

import com.ferrientregas.bucket.dto.ImageRequest;
import com.ferrientregas.bucket.dto.ImageUpdateRequest;
import com.ferrientregas.exception.IntegrityValidation;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
//    public String uploadImage(ImageRequest imageRequest) {
//        try {
//            // Validar que la imagen no sea nula o esté vacía
//            if (imageRequest.file() == null || imageRequest.file().isEmpty()) {
//                throw new IntegrityValidation("La imagen no puede estar vacía.");
//            }
//
//            // Validar extensión de la imagen
//            String originalFileName = Objects.requireNonNull(imageRequest.file()
//                    .getOriginalFilename());
//            if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
//                throw new IntegrityValidation("La imagen debe tener extensión .jpg o .png.");
//            }
//
//            // Validar el tamaño de la imagen
//            if (imageRequest.file().getSize() > 5 * 1024 * 1024) { // 5 MB en bytes
//                throw new IntegrityValidation("La imagen excede el tamaño máximo permitido de 5 MB.");
//            }
//
//            // Generar un nombre único para la imagen
//            String fileName = generateUniqueFileName(originalFileName);
//
//            // Verificar si el bucket existe
//            Bucket bucket = storage.get(bucketName);
//            if (bucket == null) {
//                throw new IntegrityValidation("El bucket no existe: " + bucketName);
//            }
//
//            // Subir la imagen al bucket
//            Blob blob = bucket
//                    .create(imageRequest.folderName() + "/" + fileName,
//                            imageRequest.file().getBytes(), imageRequest
//                                    .file().getContentType());
//
//            // Retornar el enlace público de la imagen
//            return "https://storage.googleapis.com/" + bucketName + "/" +
//                    imageRequest.folderName() + "/" + fileName;
//        } catch (Exception e) {
//            throw new IntegrityValidation("Ocurrió un error al subir la imagen: "
//                    + e.getMessage());
//        }
//    }

    /**
     * Upload list of images to bucket.
     */
    public List<String> uploadImages(ImageRequest request) {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : request.files()) {
            try {
                // Validar que la imagen no sea nula o esté vacía
                if (file == null || file.isEmpty()) {
                    throw new IntegrityValidation("La imagen no puede estar vacía.");
                }

                // Validar extensión de la imagen
                String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
                if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
                    throw new IntegrityValidation("La imagen debe tener extensión .jpg o .png.");
                }

                // Validar el tamaño de la imagen
                if (file.getSize() > 5 * 1024 * 1024) { // 5 MB en bytes
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
                Blob blob = bucket.create(request.folderName() + "/" + fileName,
                        file.getBytes(),
                        file.getContentType());

                // Agregar el enlace público de la imagen a la lista
                String imageUrl = "https://storage.googleapis.com/" + bucketName + "/" +
                        request.folderName() + "/" + fileName;
                imageUrls.add(imageUrl);
            } catch (Exception e) {
                throw new IntegrityValidation("Ocurrió un error al subir una imagen: " + e.getMessage());
            }
        }

        return imageUrls;
    }

    /**
     * Actualizar una imagen en el bucket.
     */
    public List<String> updateImages(ImageUpdateRequest request) throws IOException {
        try {
            // Validar que la lista de archivos no sea nula o vacía
            if (request.files() == null || request.files().isEmpty()) {
                throw new IntegrityValidation("Los archivos nuevos no pueden estar vacíos.");
            }

            // Validar extensiones y tamaños de los archivos
            for (MultipartFile file : request.files()) {
                if (file == null || file.isEmpty()) {
                    throw new IntegrityValidation("Uno o más archivos están vacíos.");
                }
                String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
                if (!originalFileName.endsWith(".jpg") && !originalFileName.endsWith(".png")) {
                    throw new IntegrityValidation("Todos los archivos deben ser imágenes con extensión .jpg o .png.");
                }
            }

            // Eliminar imágenes anteriores
            for (String oldFileName : request.oldFileNames()) {
                deleteImage(request.folderName(), oldFileName);
            }

            // Subir las nuevas imágenes
            return uploadImages(new ImageRequest(request.files(), request.folderName()));
        } catch (Exception e) {
            throw new IntegrityValidation("Ocurrió un error al actualizar las imágenes: " + e.getMessage());
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
