package com.server.userservice.domain.enums;

public enum ResultCode {
    // General errors
    BAD_REQUEST(400, "Bad request"),
    // User-related errors
    USER_SESSION_EXPIRED(20004, "User session has expired, please log in again"),
    INSUFFICIENT_PERMISSIONS(20005, "Insufficient permissions"),
    INCORRECT_PASSWORD(20010, "Incorrect password"),
    AUTHENTICATION_FAILED(20009, "Authentication failed"),
    EMAIL_NOT_VERIFIED(20011, "Email is not verified"),
    USER_NOT_FOUND(20027, "User does not exist"),
    USER_DISABLED(20031, "User has been disabled"),
    USER_NOT_FOUND_OR_DISABLED(20002, "User does not exist or account is disabled"),
    USER_ALREADY_EXISTS(20003, "User already exists"),
    // Authorization errors
    INVALID_TOKEN(20007, "Invalid token"),
    TOKEN_EXPIRED(20006, "Token has expired"),
    EMAIL_ALREADY_VERIFIED(20017, "Email already verified"),
    TOKEN_RESET_PASSWORD_USED(20018, "Token for resetting password has been used");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }
}
