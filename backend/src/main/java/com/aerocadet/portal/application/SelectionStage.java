package com.aerocadet.portal.application;

import java.math.BigDecimal;
import java.time.Instant;

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
@Table(name = "selection_stages")
public class SelectionStage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false)
    private CadetApplication application;
    @Column(name = "stage_order", nullable = false)
    private int stageOrder;
    @Column(nullable = false, length = 80)
    private String stage;
    @Column(nullable = false, length = 30)
    private String status;
    @Column(name = "scheduled_at")
    private Instant scheduledAt;
    @Column(precision = 6, scale = 2)
    private BigDecimal score;
    @Column(columnDefinition = "TEXT")
    private String remarks;

    protected SelectionStage() {}
    public SelectionStage(CadetApplication application, int stageOrder, String stage, String status) {
        this.application = application; this.stageOrder = stageOrder; this.stage = stage; this.status = status;
    }
    public Long getId() { return id; }
    public int getStageOrder() { return stageOrder; }
    public String getStage() { return stage; }
    public String getStatus() { return status; }
    public Instant getScheduledAt() { return scheduledAt; }
    public BigDecimal getScore() { return score; }
    public String getRemarks() { return remarks; }
}

