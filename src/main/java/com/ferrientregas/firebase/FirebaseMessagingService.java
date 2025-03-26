package com.ferrientregas.firebase;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {

    private final FirebaseMessaging firebaseMessaging;

    public String sendToToken(String token, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            return firebaseMessaging.sendAsync(message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new FirebaseMessagingException(
                    "Error al enviar mensaje al token: " + e.getMessage());
        }
    }

    public String sendToTopic(String topic, String title, String body) {
        try {
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            return firebaseMessaging.sendAsync(message).get();
        } catch (ExecutionException | InterruptedException e) {
            throw new FirebaseMessagingException(
                    "Error al enviar mensaje al tema: " + e.getMessage());
        }
    }

    public ApiFuture<BatchResponse> sendToMultipleTokens(List<String> tokens,
                                                         String title,
                                                         String body) {
        try {
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            // Usamos sendEachForMulticastAsync en lugar de sendMulticastAsync
            return firebaseMessaging.sendEachForMulticastAsync(message);
        } catch (Exception e) {
            throw new FirebaseMessagingException(
                    "Error al enviar mensaje a múltiples tokens: " +
                            e.getMessage());
        }
    }
}
