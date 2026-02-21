package com.server.cartservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddItemRequest {

    @NotBlank(message = "Product Code Is required")
    private String code;

    @NotNull(message = "Quantity is required")
    @Min(value = 1)
    private Integer quantity;

    private String name;
    private BigDecimal unitPrice;
    private String imageUrl;
}
