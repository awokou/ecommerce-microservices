package com.server.notificationservice.service;

import com.server.notificationservice.domain.dto.Product;

import java.math.BigDecimal;
import java.util.List;

public interface EmailService {
    void sendPaymentSuccessEmail(String email, String name, BigDecimal amount, String orderNumber);
    void sendOrderConfirmationEmail(String email, String name, BigDecimal totalAmount, String orderNumber, List<Product> products);
}
