package com.server.userservice.service;

public interface MailService {
    void sendEmailConfirmation(String email, String recipientName);
}
