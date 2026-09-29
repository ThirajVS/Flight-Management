package com.aerocadet.portal.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class ApplicationDtos {
    private ApplicationDtos() {}

    public record CreateRequest(@NotNull Long programId) {}
    public record DraftRequest(@Min(1) @Max(7) int step, @NotNull Map<String, Object> data) {}
    public record StatusUpdate(@NotNull ApplicationStatus status, @Size(max = 1000) String remarks) {}
    public record StageUpdate(@NotNull @jakarta.validation.constraints.Pattern(regexp = "NOT_STARTED|SCHEDULED|IN_PROGRESS|COMPLETED|QUALIFIED|NOT_QUALIFIED") String status,
                              Instant scheduledAt, @jakarta.validation.constraints.DecimalMin("0.00") @jakarta.validation.constraints.DecimalMax("100.00") BigDecimal score,
                              @Size(max = 1000) String remarks) {}

    public record Response(
            Long id, String applicationNumber, Long programId, String programName,
            ApplicationStatus status, int currentStep, Map<String, Object> draftData,
            Instant submittedAt, Instant createdAt, Instant updatedAt) {}

    public record HistoryItem(ApplicationStatus status, String changedBy, String remarks, Instant createdAt) {}
    public record StageItem(Long id, int order, String stage, String status, Instant scheduledAt, BigDecimal score, String remarks) {}
    public record Tracking(Response application, List<HistoryItem> history, List<StageItem> selectionStages) {}
}

