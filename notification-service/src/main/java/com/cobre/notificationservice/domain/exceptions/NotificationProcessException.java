package com.cobre.notificationservice.domain.exceptions;

public class NotificationProcessException extends RuntimeException {
    public NotificationProcessException(String message) {
        super(message);
    }

    public NotificationProcessException(String message, Throwable cause) {
        super(message, cause);
    }
}
