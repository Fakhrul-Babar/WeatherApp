package com.proit.application.security;

import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public record AuthenticationService(AuthenticationContext authenticationContext) {

    public Optional<UserDetails> getAuthenticatedUser() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class);
    }

    public boolean isUserLogIn(){
        return getAuthenticatedUser().isPresent();
    }

    public void logout() {
        authenticationContext.logout();
    }
}
