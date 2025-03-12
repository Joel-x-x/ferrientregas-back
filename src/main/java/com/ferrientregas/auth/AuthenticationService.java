package com.ferrientregas.auth;

import com.ferrientregas.config.JwtService;
import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.role.RoleRepository;
import com.ferrientregas.user.UserEntity;
import com.ferrientregas.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() ->
                        roleRepository.save(RoleEntity.builder()
                        .name("ROLE_USER").build()));
        var user = UserEntity.builder()
                .firstNames(request.firstNames())
                .lastNames(request.lastNames())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(userRole)
                .build();
        userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        var user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .build();
    }
}
