package com.ferrientregas.email;

import com.ferrientregas.email.dto.EmailRequest;
import com.ferrientregas.email.dto.EmailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<CompletableFuture<EmailResponse>> sendVerificationEmail(
            @RequestBody EmailRequest emailRequest
    ){
       return ResponseEntity.ok(emailService.sendVerificationEmail(emailRequest));
    }

}
