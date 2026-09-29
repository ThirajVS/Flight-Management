package com.aerocadet.portal.interview;

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
@Table(name = "interviews")
public class Interview {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false) private CadetApplication application;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "candidate_id", nullable = false) private UserAccount candidate;
    @Column(name = "scheduled_at", nullable = false) private Instant scheduledAt;
    @Column(nullable = false, length = 30) private String mode;
    @Column(nullable = false, length = 300) private String location;
    @Column(nullable = false, length = 120) private String interviewer;
    @Column(columnDefinition = "TEXT") private String remarks;
    @Column(nullable = false, length = 30) private String status;
    protected Interview() {}
    public Interview(CadetApplication application, Instant scheduledAt, String mode, String location, String interviewer, String remarks) {
        this.application=application; this.candidate=application.getCandidate(); this.scheduledAt=scheduledAt; this.mode=mode;
        this.location=location; this.interviewer=interviewer; this.remarks=remarks; this.status="SCHEDULED";
    }
    public void update(Instant scheduledAt, String mode, String location, String interviewer, String remarks, String status) {
        this.scheduledAt=scheduledAt; this.mode=mode; this.location=location; this.interviewer=interviewer; this.remarks=remarks; this.status=status;
    }
    public Long getId(){return id;} public CadetApplication getApplication(){return application;} public UserAccount getCandidate(){return candidate;}
    public Instant getScheduledAt(){return scheduledAt;} public String getMode(){return mode;} public String getLocation(){return location;}
    public String getInterviewer(){return interviewer;} public String getRemarks(){return remarks;} public String getStatus(){return status;}
}

