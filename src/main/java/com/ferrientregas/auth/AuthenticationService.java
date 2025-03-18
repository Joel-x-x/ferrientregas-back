package com.ferrientregas.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ferrientregas.auth.dto.AuthenticationRequest;
import com.ferrientregas.auth.dto.AuthenticationResponse;
import com.ferrientregas.auth.dto.RegisterRequest;
import com.ferrientregas.auth.dto.RegisterResponse;
import com.ferrientregas.config.JwtService;
import com.ferrientregas.customer.CustomerEntity;
import com.ferrientregas.customer.CustomerRepository;
import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.role.RoleNotFoundException;
import com.ferrientregas.role.RoleRepository;
import com.ferrientregas.user.UserEntity;
import com.ferrientregas.user.exception.UserNotFoundException;
import com.ferrientregas.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request)
            throws RoleNotFoundException {
        // Get role CUSTOMER
        RoleEntity role = roleRepository.findByName("CUSTOMER")
                .orElseGet(()->roleRepository.save(RoleEntity.builder().name(
                        "CUSTOMER").build()));

        // Add role to new customer
        Set<RoleEntity> roles = Set.of(role);

        // Create customer
        CustomerEntity customer =
                this.customerRepository.save(CustomerEntity.builder()
                .firstNames(request.firstNames())
                .lastNames(request.lastNames())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(roles)
                .build());

        return new RegisterResponse(
                customer.getId(),
                customer.getFirstNames(),
                customer.getLastNames(),
                customer.getEmail(),
                customer.getEmailConfirmed()
        );
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request)
            throws UserNotFoundException {
        // Create manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        // Find user
        UserEntity user = userRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(UserNotFoundException::new);

        // Generate JWT
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .roles(user.getRoles())
                .verified(user.getEmailConfirmed().toString())
                .build();
    }

    public void refreshToken(HttpServletResponse response,
                             HttpServletRequest request) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        /*
         * When you got an userEmail and the user isn't authenticated, you get
         * the userDetails from the database, after you check if the user and the
         * token are valid, we return an UsernamePasswordAuthenticationToken
         * and after that just update the SecurityContextHolder with this
         * authentication token.
         * */

        if(userEmail != null ) {
            var user = this.userRepository.findByEmail(userEmail);

            if(jwtService.validateToken(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(),
                        authResponse);
            }
        }
    }
}
