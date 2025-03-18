package com.ferrientregas.user;

import com.ferrientregas.role.RoleRepository;
import com.ferrientregas.user.dto.*;
import com.ferrientregas.user.exception.UserNotFoundException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * This method is used to verify the Email of the user with the token
     * stored in the database
     * @param  request
     * @return VerificationResponse
     * @throws UserNotFoundException
     */
    public VerificationResponse verifyEmailToken(VerificationRequest request)
    throws UserNotFoundException {
        UserEntity user = userRepository.findByToken(request.token());
        if(user != null){
            user.setEmailConfirmed(true);
            userRepository.save(user);
        }else{
            throw new UserNotFoundException();
        }
        return VerificationResponse.builder()
                .message("Email Verified")
                .build();
    }

    /**
     *This method is used to get all the information of a User by ID
     * @param id
     * @return UserResponse
     * @throws UserNotFoundException
     */
   public UserResponse getUser(UUID id) throws UserNotFoundException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        return  new UserResponse(
                user.getId(),
                user.getFirstNames(),
                user.getLastNames(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileImage(),
                user.getEmailConfirmed(),
                user.getRoles()
        );
   }

   public Page<UserResponse> listUsers(Pageable pageable){
      return this.userRepository.findAllByDeletedFalse(pageable)
              .map(user-> new UserResponse(
                      user.id(),
                      user.firstNames(),
                      user.lastNames(),
                      user.email(),
                      user.password(),
                      user.profileImage(),
                      user.emailConfirmed(),
                      user.roles()
              ));
   }


   public UserResponse createUser(UserRequest userRequest){
     UserEntity user = userRepository.save(UserEntity.builder()
                     .firstNames(userRequest.firstNames())
                     .lastNames(userRequest.lastNames())
                     .email(userRequest.email())
                     .password(userRequest.password())
                     .profileImage(userRequest.profileImage())
                     .emailConfirmed(false)
                     .roles(this.roleRepository.findRoleEntityByName(userRequest.role()))
             .build());

       return  new UserResponse(
               user.getId(),
               user.getFirstNames(),
               user.getLastNames(),
               user.getEmail(),
               user.getPassword(),
               user.getProfileImage(),
               user.getEmailConfirmed(),
               user.getRoles()
       );
   }

   public UserResponse updateUser(UUID id, UserUpdateRequest userUpdateRequest)
   throws UserNotFoundException {
      UserEntity user = userRepository.findById(id)
              .orElseThrow(UserNotFoundException::new);

      if(!StringUtils.isBlank(user.getFirstNames())){
          user.setFirstNames(userUpdateRequest.firstNames());
      }
      if(!StringUtils.isBlank(user.getLastNames())){
          user.setLastNames(userUpdateRequest.lastNames());
      }
      if(!StringUtils.isBlank(user.getEmail())){
          user.setEmail(userUpdateRequest.email());
      }
      if(!StringUtils.isBlank(user.getPassword())){
          user.setPassword(userUpdateRequest.password());
      }
      user.setProfileImage(userUpdateRequest.profileImage());
      user.setEmailConfirmed(userUpdateRequest.emailConfirmed());
      user.setRoles(this.roleRepository
              .findRoleEntityByName(userUpdateRequest.role()));

      userRepository.save(user);

       return  new UserResponse(
               user.getId(),
               user.getFirstNames(),
               user.getLastNames(),
               user.getEmail(),
               user.getPassword(),
               user.getProfileImage(),
               user.getEmailConfirmed(),
               user.getRoles()
       );
   }

   public Boolean deleteUser(UUID id) throws UserNotFoundException {
       UserEntity user = userRepository.findById(id)
               .orElseThrow(UserNotFoundException::new);

       user.setDeleted(true);
       userRepository.save(user);
       return true;
   }
}
