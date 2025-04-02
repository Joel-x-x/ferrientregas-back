package com.ferrientregas.user;

import com.ferrientregas.user.dto.UserResponse;
import com.ferrientregas.user.dto.VerificationResponse;

public class UserMapper {
    public static UserResponse toUserResponse(UserEntity user) {
        return new UserResponse(
                user.getId(),
                user.getFirstNames(),
                user.getLastNames(),
                user.getEmail(),
                user.getProfileImage(),
                user.getEmailConfirmed(),
                user.getRoles()
        );
    }

    public static VerificationResponse toVerificationResponse(){
        return VerificationResponse.builder()
                .message("Email Verified")
                .build();

    }
}
