package com.ferrientregas.user;

import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.role.RoleRepository;
import com.ferrientregas.user.dto.*;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private static final Set<String> ROLES = Set.of("ADMIN", "DRIVER",
            "EMPLOYEE");
    private final PasswordEncoder passwordEncoder;

    public UserResponse getUser(UUID id){
        return this.userRepository.findById(id)
                .map(UserMapper::toUserResponse)
                .orElseThrow(()->
                        new EntityNotFoundException("User with id " + id +
                                " not found"));

   }

   public Page<UserResponse> listUsers(Pageable pageable){
      return this.userRepository.findAllByDeletedIsFalse(pageable)
              .map(UserMapper::toUserResponse);
   }

   public UserResponse createUser(UserRequest userRequest){
     UserEntity user = createAndSaveUser(userRequest);
     return UserMapper.toUserResponse(user);
   }

   public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest)
   {
      UserEntity user = getUserEntityById(id);
      updateUserFields(user, userUpdateRequest);
      this.userRepository.save(user);

      return UserMapper.toUserResponse(user);
   }

   public void deleteUser(UUID id){
       UserEntity user = getUserEntityById(id);

       user.setDeleted(true);
       userRepository.save(user);
   }

    public VerificationResponse verifyEmailToken(VerificationRequest request) {
        UserEntity user = userRepository.findByToken(request.token())
                .orElseThrow(()->
                        new EntityNotFoundException("User with token " +
                                request.token() + " not found"));

        user.setEmailConfirmed(true);
        userRepository.save(user);
        return UserMapper.toVerificationResponse();
    }

    private UserEntity getUserEntityById(UUID id){
      return this.userRepository.findById(id)
              .orElseThrow(()->
                      new EntityNotFoundException("User with id " + id +
                              " not found"));
    }

    private void updateUserFields(UserEntity user, UserUpdateRequest userUpdateRequest){
        if(!StringUtils.isBlank(userUpdateRequest.firstNames())){
            user.setFirstNames(userUpdateRequest.firstNames());
        }
        if(!StringUtils.isBlank(userUpdateRequest.lastNames())){
            user.setLastNames(userUpdateRequest.lastNames());
        }
        if(!StringUtils.isBlank(userUpdateRequest.email())){
            user.setEmail(userUpdateRequest.email());
        }
        if(!StringUtils.isBlank(userUpdateRequest.password())){
            user.setPassword(userUpdateRequest.password());
        }
        user.setProfileImage(userUpdateRequest.profileImage());
        user.setEmailConfirmed(userUpdateRequest.emailConfirmed());

        user.setRoles(getOrCreateRole(userUpdateRequest.role()));

        userRepository.save(user);
    }

    private UserEntity createAndSaveUser(UserRequest userRequest){
        return this.userRepository.save(UserEntity.builder()
                .firstNames(userRequest.firstNames())
                .lastNames(userRequest.lastNames())
                .email(userRequest.email())
                .password(passwordEncoder.encode(userRequest.password()))
                .profileImage(userRequest.profileImage())
                .emailConfirmed(false)
                .roles(getOrCreateRole(userRequest.role()))
                .build());
    }

    private Set<RoleEntity> getOrCreateRole(String role){
        return Collections.singleton(
                this.roleRepository.findByName(role)
                        .orElseGet(() ->
                                ROLES.stream()
                                        .filter(r -> r.equals(role))
                                        .findFirst()
                                        .map(r -> roleRepository.save(new RoleEntity(r)))
                                        .orElseThrow(() -> new EntityNotFoundException("Role not found")
                        )
        ));
    }
}
