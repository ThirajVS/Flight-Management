package com.aerocadet.portal.notification;

import java.util.List;

import com.aerocadet.portal.common.ResourceNotFoundException;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserAccountRepository userAccountRepository;

    public NotificationService(NotificationRepository notificationRepository, UserAccountRepository userAccountRepository) {
        this.notificationRepository = notificationRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void create(UserAccount recipient, String type, String title, String message) {
        notificationRepository.save(new Notification(recipient, type, title, message));
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> list(String email) {
        return notificationRepository.findByRecipientEmailIgnoreCaseOrderByCreatedAtDesc(email)
                .stream().map(NotificationResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public long unreadCount(String email) {
        return notificationRepository.countByRecipientEmailIgnoreCaseAndReadFalse(email);
    }

    @Transactional
    public NotificationResponse markRead(String email, Long id) {
        Notification notification = notificationRepository.findById(id)
                .filter(item -> item.getRecipient().getEmail().equalsIgnoreCase(email))
                .orElseThrow(() -> new ResourceNotFoundException("Notification was not found"));
        notification.markRead();
        return NotificationResponse.from(notification);
    }
}

