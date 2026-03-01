package com.server.orderservice.client;

import com.server.orderservice.domain.dto.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "user-service", path = "${custom.config.user.url}")
public interface UserClient {
    @GetMapping(path = "/{userId}")
    Optional<UserResponse> findUser(@PathVariable Long userId);
}
