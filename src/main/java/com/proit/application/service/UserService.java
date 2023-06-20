package com.proit.application.service;

import com.proit.application.entity.UserEntity;

import java.util.Optional;

public interface UserService {

    Optional<UserEntity> getUser(String username);

    Long getLogedInUserId();
}
