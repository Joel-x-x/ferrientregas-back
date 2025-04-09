package com.ferrientregas.user;

import com.ferrientregas.exception.ResultResponse;
import com.ferrientregas.user.dto.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResultResponse<Page<UserResponse>,String>> findAll(Pageable pageable) {
            return ResponseEntity.ok(ResultResponse.success(
                    this.userService.listUsers(pageable),200));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<ResultResponse<Page<UserResponse>,String>> findAllByRole(
            Pageable pageable,
            @PathVariable
            String role) {
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.listByRole(pageable, role),200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<UserResponse,String>> findById(
            @PathVariable UUID id){
       return ResponseEntity.ok(ResultResponse.success(
               this.userService.getUser(id),200
       ));
    }

    @GetMapping("/current-user")
    public ResponseEntity<ResultResponse<UserResponse,String>> findByJWT(
            Authentication authentication
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.getUserByJWT(authentication),
               200
        ));
    }

   @PostMapping
   @ResponseStatus(HttpStatus.CREATED)
   public ResponseEntity<ResultResponse<Object,String>> create(
           @Valid @RequestBody UserRequest userRequest
           ){
       UserResponse userResponse = this.userService.createUser(userRequest);
       URI location = ServletUriComponentsBuilder
               .fromCurrentRequest()
               .path("/{id}")
               .buildAndExpand(userResponse.id())
               .toUri();

       return ResponseEntity.created(location)
               .body(
                       ResultResponse.success(userResponse, 201)
               );
   }

    @PostMapping("/email-verification")
    public ResponseEntity<ResultResponse<Object,String>> verifyEmailToken(
            @RequestBody VerificationRequest request
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.verifyEmailToken(request),200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> update(
           @PathVariable UUID id,
           @Valid @RequestBody UserUpdateRequest userRequest
    ){
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.updateUser(id,userRequest),
                200
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
