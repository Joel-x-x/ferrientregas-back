package com.ferrientregas.driver;

import com.ferrientregas.role.RoleEntity;
import com.ferrientregas.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findAllDriversByRole(RoleEntity role);
    Optional<UserEntity> findDriverById(UUID id);
}
