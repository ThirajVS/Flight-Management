package com.aerocadet.portal.application;

import java.time.Instant;

import com.aerocadet.portal.user.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_status_history")
public class ApplicationStatusHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false)
    private CadetApplication application;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private ApplicationStatus status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "changed_by", nullable = false)
    private UserAccount changedBy;
    @Column(columnDefinition = "TEXT")
    private String remarks;
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected ApplicationStatusHistory() {}
    public ApplicationStatusHistory(CadetApplication application, ApplicationStatus status, UserAccount changedBy, String remarks) {
        this.application = application; this.status = status; this.changedBy = changedBy; this.remarks = remarks;
    }
    @PrePersist void setCreatedAt() { createdAt = Instant.now(); }
    public Long getId() { return id; }
    public ApplicationStatus getStatus() { return status; }
    public UserAccount getChangedBy() { return changedBy; }
    public String getRemarks() { return remarks; }
    public Instant getCreatedAt() { return createdAt; }
}

