package com.proit.application.service;

import com.proit.application.domain.Location;
import com.proit.application.entity.UserFavoriteLocationEntity;

import java.util.List;

public interface UserFavoriteLocationService {

    List<UserFavoriteLocationEntity> getFavoriteLocationByUser(String username);
    Long saveUserFavoriteLocation(Location location);

    void deleteUserFavoriteLocation(Long favLocationId);

    boolean existsByLocationIdAndUsername(Long locationId, String username);
    long countByLocationIdAndUsername(Long locationId, String username);
    long countByUsername(String username);
}
