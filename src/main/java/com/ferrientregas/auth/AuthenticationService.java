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
import java.util.Collections;
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
    private final ObjectMapper objectMapper;
    private static final String CUSTOMER_ROLE = "CUSTOMER";

    public RegisterResponse register(RegisterRequest request) {
        //Generate Role
        RoleEntity role = getOrCreateRole();
        // Add role to new customer
        Set<RoleEntity> roles = Collections.singleton(role);
        // Create customer
        CustomerEntity customer = createAndSaveCustomer(roles,request);
        return createRegisterResponseByCustomer(customer);
    }

    private RoleEntity getOrCreateRole(){

        return this.roleRepository.findByName(CUSTOMER_ROLE)
                .orElseGet(()->roleRepository.save(RoleEntity.builder()
                        .name(CUSTOMER_ROLE).build()));
    }

    private CustomerEntity createAndSaveCustomer(Set<RoleEntity> roles,
                                            RegisterRequest registerRequest) {

        return this.customerRepository.save(CustomerEntity.builder()
                .firstNames(registerRequest.firstNames())
                .lastNames(registerRequest.lastNames())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(roles)
                .build());
    }

    private RegisterResponse createRegisterResponseByCustomer(CustomerEntity customer) {
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
        return userRepository.findByEmailIgnoreCase(request.email())
                .map(user->{
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.email(),
                                    request.password()
                            )
                    );

                    String token = jwtService.generateToken(user);
                    String refreshToken = jwtService.generateRefreshToken(user);

                    return createAuthResponseByUser(token,refreshToken,user);
                }).orElseThrow(UserNotFoundException::new);
    }

    private AuthenticationResponse createAuthResponseByUser(String accessToken
            ,String refreshToken, UserEntity user) {
       return AuthenticationResponse.builder()
               .accessToken(accessToken)
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
                String accessToken = jwtService.generateToken(user);

                AuthenticationResponse authResponse =
                        AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                objectMapper.writeValue(response.getOutputStream(),authResponse);
            }
        }
    }
}
