package com.ferrientregas.user;

import com.ferrientregas.user.dto.VerificationRequest;
import com.ferrientregas.user.dto.VerificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/email-verification")
    public ResponseEntity<VerificationResponse> verifyEmailToken(
            @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(userService.verifyEmailToken(request));
    }
}
