package com.ferrientregas.email;

import com.ferrientregas.user.UserEntity;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.concurrent.CompletableFuture;

import static com.ferrientregas.email.EmailUtils.getEmailMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public CompletableFuture<EmailResponse> sendVerificationEmail(EmailRequest emailRequest) {
        try{
            UserEntity user = userRepository.findByEmail(emailRequest.to());
            SimpleMailMessage message = new SimpleMailMessage();
            message.setSubject("Verificación de email");
            message.setFrom(fromEmail);
            message.setTo(emailRequest.to());
            message.setText(getEmailMessage(emailRequest.name(), user.getToken()));
            mailSender.send(message);
        }catch(Exception e){
           throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(EmailResponse.builder()
                .message("Mail enviado com exito!")
                .build());
    }
}
