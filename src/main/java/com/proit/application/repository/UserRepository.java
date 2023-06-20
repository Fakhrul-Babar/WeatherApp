package com.proit.application.repository;

import com.proit.application.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    <T> Optional<T> findByUsername(String username, Class<T> clazz);

    boolean existsByUsername(String username);
}
