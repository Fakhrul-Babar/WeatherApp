package com.proit.application.repository;

import com.proit.application.entity.UserFavoriteLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFavoriteLocationRepository extends JpaRepository<UserFavoriteLocationEntity, Long> {

    boolean existsByLocationIdAndUser_Username(Long locationId, String username);
}
