package com.ferrientregas.user;

import com.ferrientregas.exception.ResultResponse;
import com.ferrientregas.user.dto.*;
import com.ferrientregas.user.exception.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResultResponse<Object,String>> findAll(Pageable pageable){
            return ResponseEntity.ok(ResultResponse.success(
                    this.userService.listUsers(pageable),200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> findById(
            @PathVariable UUID id) throws UserNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.userService.getUser(id),200
       ));
    }

   @PostMapping
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
    ) throws UserNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.verifyEmailToken(request),200));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> update(
           @PathVariable UUID id,
           @Valid @RequestBody UserUpdateRequest userRequest
    ) throws UserNotFoundException {
        return ResponseEntity.ok(ResultResponse.success(
                this.userService.updateUser(id,userRequest),
                200
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResultResponse<Object,String>> delete(
            @PathVariable UUID id
    ) throws UserNotFoundException {
       return ResponseEntity.ok(ResultResponse.success(
               this.userService.deleteUser(id),200
       ));
    }


}
