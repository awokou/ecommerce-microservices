package com.server.userservice.service;

import com.server.userservice.domain.dto.external.UserDto;
import com.server.userservice.domain.dto.response.AuthResponse;
import com.server.userservice.domain.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    AuthResponse createUser(UserDto request);

    List<UserResponse> findAll();

    UserResponse findAllById(Long userId);


}
