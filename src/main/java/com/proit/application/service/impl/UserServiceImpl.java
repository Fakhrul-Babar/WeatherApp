package com.proit.application.service.impl;

import com.proit.application.domain.projection.UserIdProjection;
import com.proit.application.entity.UserEntity;
import com.proit.application.repository.UserRepository;
import com.proit.application.security.AuthenticationService;
import com.proit.application.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    public UserServiceImpl(
            AuthenticationService authenticationService,
            UserRepository userRepository
    ) {
        this.authenticationService = authenticationService;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> getUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Long getLogedInUserId() {
        UserDetails user = authenticationService.getAuthenticatedUser()
                .orElseThrow(() -> new UsernameNotFoundException("No logedIn user found"));
        return userRepository.findByUsername(user.getUsername(), UserIdProjection.class)
                .map(UserIdProjection::getId)
                .orElse(0l);
    }
}
