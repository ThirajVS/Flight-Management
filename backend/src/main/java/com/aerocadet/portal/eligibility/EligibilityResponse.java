package com.aerocadet.portal.eligibility;

import java.time.Instant;
import java.util.List;

public record EligibilityResponse(
        Long id,
        Long programId,
        String programName,
        EligibilityStatus overallStatus,
        List<EligibilityCheck> checks,
        Instant checkedAt) {

    public record EligibilityCheck(
            String criterion,
            CheckStatus status,
            String message) {
    }
}

