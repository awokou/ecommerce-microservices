package com.server.userservice.repository;

import com.server.userservice.domain.entity.ConfirmationEmail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfirmationEmailRepository extends JpaRepository<ConfirmationEmail, Long> {
    Optional<ConfirmationEmail> findByToken(String token);
}
