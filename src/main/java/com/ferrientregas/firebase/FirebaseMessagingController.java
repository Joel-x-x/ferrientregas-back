package com.ferrientregas.firebase;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor
public class FirebaseMessagingController {

    private final FirebaseMessagingService firebaseMessagingService;

    @PostMapping("/send-to-token")
    public ResponseEntity<String> sendToToken(@RequestBody String token,
                                              @RequestBody String title,
                                              @RequestBody String body) {
        try {
            String response = firebaseMessagingService.sendToToken(token, title, body);
            return ResponseEntity.ok("Mensaje enviado con ID: " + response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al enviar mensaje: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-topic")
    public ResponseEntity<String> sendToTopic(@RequestBody String topic,
                                              @RequestBody String title,
                                              @RequestBody String body) {
        try {
            String response = firebaseMessagingService.sendToTopic(topic, title, body);
            return ResponseEntity.ok("Mensaje enviado al tema con ID: " + response);
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al enviar mensaje: " + e.getMessage());
        }
    }

    @PostMapping("/send-to-multiple-tokens")
    public ResponseEntity<String> sendToMultipleTokens(@RequestBody List<String> tokens,
                                                       @RequestBody String title,
                                                       @RequestBody String body) {
        try {
            ApiFuture<BatchResponse> futureResponse = firebaseMessagingService.sendToMultipleTokens(tokens, title, body);
            BatchResponse response = futureResponse.get();
            return ResponseEntity.ok("Mensajes enviados: " + response.getSuccessCount());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al enviar mensajes: " + e.getMessage());
        }
    }
}
