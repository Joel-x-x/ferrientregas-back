package com.ferrientregas.auth;

import com.ferrientregas.exception.ResultResponse;
import com.ferrientregas.role.RoleNotFoundException;
import com.ferrientregas.user.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResultResponse<Object,String>> register(
            @RequestBody RegisterRequest request
    ) throws RoleNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.authenticationService.register(request), 201)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResultResponse<Object,String>> login(
            @RequestBody AuthenticationRequest request
    ) throws UserNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.authenticationService.authenticate(request), 200));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
       authenticationService.refreshToken(response, request);
    }
}
