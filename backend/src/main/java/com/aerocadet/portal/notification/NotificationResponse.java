package com.aerocadet.portal.notification;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String type,
        String title,
        String message,
        boolean read,
        Instant createdAt) {

    static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(), notification.getType(), notification.getTitle(),
                notification.getMessage(), notification.isRead(), notification.getCreatedAt());
    }
}

