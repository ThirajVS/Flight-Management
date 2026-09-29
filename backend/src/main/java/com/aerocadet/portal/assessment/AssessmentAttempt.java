package com.aerocadet.portal.assessment;

import java.math.BigDecimal;
import java.time.Instant;

import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.user.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assessment_attempts")
public class AssessmentAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false) private CadetApplication application;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "candidate_id", nullable = false) private UserAccount candidate;
    @Column(name = "started_at", nullable = false) private Instant startedAt;
    @Column(name = "submitted_at", nullable = false) private Instant submittedAt;
    @Column(nullable = false, precision = 6, scale = 2) private BigDecimal score;
    @Column(nullable = false, length = 30) private String status;
    @Column(name = "answers_json", nullable = false, columnDefinition = "TEXT") private String answersJson;
    protected AssessmentAttempt() {}
    public AssessmentAttempt(CadetApplication application, UserAccount candidate, Instant startedAt, BigDecimal score, String status, String answersJson) {
        this.application=application; this.candidate=candidate; this.startedAt=startedAt; this.submittedAt=Instant.now(); this.score=score; this.status=status; this.answersJson=answersJson;
    }
    public Long getId() { return id; }
    public BigDecimal getScore() { return score; }
    public String getStatus() { return status; }
    public Instant getSubmittedAt() { return submittedAt; }
}

