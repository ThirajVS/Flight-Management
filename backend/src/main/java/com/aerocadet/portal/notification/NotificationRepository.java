package com.aerocadet.portal.notification;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientEmailIgnoreCaseOrderByCreatedAtDesc(String email);
    long countByRecipientEmailIgnoreCaseAndReadFalse(String email);
}

