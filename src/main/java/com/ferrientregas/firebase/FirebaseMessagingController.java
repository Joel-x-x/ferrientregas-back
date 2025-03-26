package com.ferrientregas.firebase;

import com.ferrientregas.firebase.dto.MultipleTokensRequest;
import com.ferrientregas.firebase.dto.TokenRequest;
import com.ferrientregas.firebase.dto.TopicRequest;
import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> sendToToken(@Valid @RequestBody TokenRequest request) {
        String response =
                firebaseMessagingService.sendToToken(request.token(),
                        request.title(), request.body());
        return ResponseEntity.ok("Mensaje enviado con ID: " + response);
    }

    @PostMapping("/send-to-topic")
    public ResponseEntity<String> sendToTopic(@Valid @RequestBody TopicRequest request) {
        String response =
                firebaseMessagingService.sendToTopic(request.topic(),
                        request.title(), request.body());
        return ResponseEntity.ok("Mensaje enviado al tema con ID: " + response);
    }

    @PostMapping("/send-to-multiple-tokens")
    public ResponseEntity<String> sendToMultipleTokens(@Valid @RequestBody MultipleTokensRequest request) {
        ApiFuture<BatchResponse> futureResponse =
                firebaseMessagingService.sendToMultipleTokens(
                        request.tokens(),
                        request.title(),
                        request.body());
        try {
            BatchResponse response = futureResponse.get();
            return ResponseEntity.ok("Mensajes enviados: " + response.getSuccessCount());
        } catch (ExecutionException | InterruptedException e) {
            return ResponseEntity.internalServerError().body("Error al enviar mensajes: " + e.getMessage());
        }
    }
}
