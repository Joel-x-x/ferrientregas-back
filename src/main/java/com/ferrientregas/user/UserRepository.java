package com.ferrientregas.user;

import com.ferrientregas.user.dto.UserResponse;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmailIgnoreCase(String email);
    UserEntity findUserEntityById(UUID id);
    UserEntity findByEmail(String token);
    Optional<UserEntity> findByToken(String token);
    Page<UserEntity> findAllByDeletedIsFalse(Pageable pageable);
}
