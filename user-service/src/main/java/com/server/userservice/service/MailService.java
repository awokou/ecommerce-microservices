package com.server.userservice.service;

public interface MailService {
    void sendConfirmRegistrationAccount(String email, String recipientName);
}
