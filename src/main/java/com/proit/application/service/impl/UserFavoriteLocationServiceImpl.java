package com.proit.application.service.impl;

import com.proit.application.domain.Location;
import com.proit.application.entity.UserEntity;
import com.proit.application.entity.UserFavoriteLocationEntity;
import com.proit.application.repository.UserFavoriteLocationRepository;
import com.proit.application.security.AuthenticationService;
import com.proit.application.service.UserFavoriteLocationService;
import com.proit.application.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserFavoriteLocationServiceImpl implements UserFavoriteLocationService {

    private final UserFavoriteLocationRepository userFavoriteLocationRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;

    public UserFavoriteLocationServiceImpl(
            UserFavoriteLocationRepository userFavoriteLocationRepository,
            UserService userService,
            AuthenticationService authenticationService) {
        this.userFavoriteLocationRepository = userFavoriteLocationRepository;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<UserFavoriteLocationEntity> getFavoriteLocationByUser(String username) {
        return userFavoriteLocationRepository.findAllByUser_Username(username);
    }

    @Override
    public Long saveUserFavoriteLocation(Location location) {
        UserFavoriteLocationEntity favoriteLocation = new UserFavoriteLocationEntity();
        UserEntity user = userService.getUser(authenticationService.getLogedInUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        favoriteLocation.setLocationId(location.getId());
        favoriteLocation.setLatitude(location.getLatitude());
        favoriteLocation.setLongitude(location.getLongitude());
        favoriteLocation.setUser(user);
        String locationDetails = String.format("%s, %s, %s", location.getName(), location.getAdminDetails(), location.getCountry());
        favoriteLocation.setLocationDetails(locationDetails);

        return userFavoriteLocationRepository.save(favoriteLocation).getId();
    }

    @Override
    public void deleteUserFavoriteLocation(Long favLocationId) {
        userFavoriteLocationRepository.deleteById(favLocationId);
    }

    @Override
    public boolean existsByLocationIdAndUsername(Long locationId, String username) {
        return userFavoriteLocationRepository.existsByLocationIdAndUser_Username(locationId, username);
    }

    @Override
    public long countByLocationIdAndUsername(Long locationId, String username) {
        return userFavoriteLocationRepository.countByLocationIdAndUser_Username(locationId, username);
    }

    @Override
    public long countByUsername(String username) {
        return userFavoriteLocationRepository.countByUser_Username(username);
    }
}
