package com.server.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    CONFIRM_REGISTRATION("confirm-registration.html", "Confirm your registration"),
    PASSWORD_RESET("password-reset.html", "Reset your password"),
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Payment completed successfully"),
    ORDER_CONFIRMATION("order-confirmation.html", "Order completed successfully");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
