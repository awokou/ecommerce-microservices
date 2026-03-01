package com.server.paymentservice.domain.dto;

import com.server.paymentservice.domain.enums.PaymentMethod;

import java.math.BigDecimal;

public record PaymentNotification(String orderReference,
                                  BigDecimal amount,
                                  PaymentMethod paymentMethod,
                                  String name,
                                  String email) {
}
