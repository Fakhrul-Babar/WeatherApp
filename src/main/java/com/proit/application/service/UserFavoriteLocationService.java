package com.proit.application.service;

import com.proit.application.domain.Location;

public interface UserFavoriteLocationService {

    Long saveUserFavoriteLocation(Location location);

    void deleteUserFavoriteLocation(Long favLocationId);

    boolean existsByLocationIdAndUsername(Long locationId, String username);
}
