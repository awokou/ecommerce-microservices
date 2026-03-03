package com.server.userservice.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResendEmailConfirmationRequest {
    @NotNull(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;
}
