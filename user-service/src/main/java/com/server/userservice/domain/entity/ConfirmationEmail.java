package com.server.userservice.domain.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "confirmation_emails")
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmationEmail implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;
    private boolean revoked;

    private String email;

    private LocalDateTime confirmedAt;
    private LocalDateTime expiredAt;
}
