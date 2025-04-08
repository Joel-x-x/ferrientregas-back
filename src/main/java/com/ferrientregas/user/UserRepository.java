package com.ferrientregas.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query(
            """
            select u from UserEntity u join u.roles r where u.deleted = false and r.name = :role
            """
    )
    Page<UserEntity> findAllByDeletedIsFalseAndRolesNameContains(Pageable pageable, String role);

    /** Reports **/
    long countByRoles_Name(String role);
}
