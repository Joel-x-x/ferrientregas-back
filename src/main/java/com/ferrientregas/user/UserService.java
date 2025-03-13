package com.ferrientregas.user;

import com.ferrientregas.config.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public VerificationResponse verifyEmailToken(VerificationRequest request){
        UserEntity user = userRepository.findByToken(request.token());
        if(user == null){

        }
        user.setEmailConfirmed(true);
        userRepository.save(user);
        return VerificationResponse.builder()
                .message("Email Verified")
                .build();
    }
}
