package com.aerocadet.portal.audit;

import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserAccountRepository userAccountRepository;

    public AuditService(AuditLogRepository auditLogRepository, UserAccountRepository userAccountRepository) {
        this.auditLogRepository = auditLogRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public void record(String actorEmail, String action, String entityType, String entityId, String details) {
        UserAccount actor = actorEmail == null ? null : userAccountRepository.findByEmailIgnoreCase(actorEmail).orElse(null);
        auditLogRepository.save(new AuditLog(actor, action, entityType, entityId, details));
    }
}

