package com.aerocadet.portal.application;

import java.time.Instant;

import com.aerocadet.portal.program.CadetProgram;
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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "applications")
public class CadetApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_number", nullable = false, unique = true, length = 30)
    private String applicationNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private UserAccount candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private CadetProgram program;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ApplicationStatus status = ApplicationStatus.DRAFT;

    @Column(name = "current_step", nullable = false)
    private int currentStep = 1;

    @Column(name = "draft_data_json", nullable = false, columnDefinition = "TEXT")
    private String draftDataJson = "{}";

    @Column(name = "submitted_at")
    private Instant submittedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected CadetApplication() {
    }

    public CadetApplication(String applicationNumber, UserAccount candidate, CadetProgram program) {
        this.applicationNumber = applicationNumber;
        this.candidate = candidate;
        this.program = program;
    }

    @PrePersist
    void setCreatedAt() { createdAt = updatedAt = Instant.now(); }

    @PreUpdate
    void setUpdatedAt() { updatedAt = Instant.now(); }

    public void saveDraft(int step, String json) {
        this.currentStep = step;
        this.draftDataJson = json;
    }

    public void submit() {
        this.status = ApplicationStatus.SUBMITTED;
        this.currentStep = 7;
        this.submittedAt = Instant.now();
    }

    public void changeStatus(ApplicationStatus status) { this.status = status; }

    public Long getId() { return id; }
    public String getApplicationNumber() { return applicationNumber; }
    public UserAccount getCandidate() { return candidate; }
    public CadetProgram getProgram() { return program; }
    public ApplicationStatus getStatus() { return status; }
    public int getCurrentStep() { return currentStep; }
    public String getDraftDataJson() { return draftDataJson; }
    public Instant getSubmittedAt() { return submittedAt; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}

