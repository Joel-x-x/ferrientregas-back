package com.ferrientregas.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class GoogleCloudStorageConfiguration {

    @Value("${gcp.GOOGLE_APPLICATION_CREDENTIALS}")
    private String credentialsPath;

    @Bean
    public Storage storage() throws IOException {

        // Ruta al archivo JSON (puedes obtenerla de application.properties o directamente aquí)

        if (credentialsPath == null) {
            throw new IllegalStateException("La variable GOOGLE_APPLICATION_CREDENTIALS no está configurada.");
        }

        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(credentialsPath));
        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .build()
                .getService();
    }
}
