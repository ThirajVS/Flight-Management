package com.aerocadet.portal.eligibility;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "eligibility_results")
public class EligibilityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private UserAccount candidate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "program_id", nullable = false)
    private CadetProgram program;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EligibilityStatus status;

    @Column(name = "checks_json", nullable = false, columnDefinition = "TEXT")
    private String checksJson;

    @Column(name = "checked_at", nullable = false, updatable = false)
    private Instant checkedAt;

    protected EligibilityResult() {
    }

    public EligibilityResult(UserAccount candidate, CadetProgram program, EligibilityStatus status, String checksJson) {
        this.candidate = candidate;
        this.program = program;
        this.status = status;
        this.checksJson = checksJson;
    }

    @PrePersist
    void setCheckedAt() {
        this.checkedAt = Instant.now();
    }

    public Long getId() { return id; }
    public UserAccount getCandidate() { return candidate; }
    public CadetProgram getProgram() { return program; }
    public EligibilityStatus getStatus() { return status; }
    public String getChecksJson() { return checksJson; }
    public Instant getCheckedAt() { return checkedAt; }
}

