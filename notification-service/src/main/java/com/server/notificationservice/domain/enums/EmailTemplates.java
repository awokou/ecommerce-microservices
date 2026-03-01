package com.server.notificationservice.domain.enums;

import lombok.Getter;

@Getter
public enum EmailTemplates {
    PAYMENT_CONFIRMATION("payment-confirmation.html", "Pago procesado exitosamente"),
    ORDER_CONFIRMATION("order-confirmation.html", "Confirmación de orden");

    private final String template;
    private final String subject;

    EmailTemplates(String template, String subject) {
        this.template = template;
        this.subject = subject;
    }
}
