package com.ferrientregas.user.dto;

import com.ferrientregas.user.UserEntity;

public class UserMapper {
    public static UserResponse toUserResponse(UserEntity user) {
        return new UserResponse(
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

    public static VerificationResponse toVerificationResponse(){
        return VerificationResponse.builder()
                .message("Email Verified")
                .build();

    }
}
