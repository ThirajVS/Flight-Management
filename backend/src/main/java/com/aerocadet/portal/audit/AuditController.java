package com.aerocadet.portal.audit;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditLogRepository auditLogRepository;

    public AuditController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<AuditResponse>> list() {
        return ResponseEntity.ok(auditLogRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(log -> new AuditResponse(
                        log.getId(),
                        log.getActor() == null ? "SYSTEM" : log.getActor().getEmail(),
                        log.getAction(), log.getEntityType(), log.getEntityId(), log.getDetails(), log.getCreatedAt()))
                .toList());
    }

    public record AuditResponse(Long id, String actor, String action, String entityType,
                                String entityId, String details, Instant createdAt) {}
}

