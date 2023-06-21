package com.proit.application.repository;

import com.proit.application.entity.UserFavoriteLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFavoriteLocationRepository extends JpaRepository<UserFavoriteLocationEntity, Long> {

    List<UserFavoriteLocationEntity> findAllByUser_Username(String username);
    boolean existsByLocationIdAndUser_Username(Long locationId, String username);

    long countByLocationIdAndUser_Username(Long locationId, String username);
    long countByUser_Username(String username);
}
