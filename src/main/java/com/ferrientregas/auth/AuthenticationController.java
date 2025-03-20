package com.ferrientregas.auth;

import com.ferrientregas.auth.dto.AuthenticationRequest;
import com.ferrientregas.auth.dto.RegisterRequest;
import com.ferrientregas.exception.ResultResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResultResponse<Object,String>> register(
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.authenticationService.register(request), 201)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResultResponse<Object,String>> login(
            @RequestBody AuthenticationRequest request
    ){
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
