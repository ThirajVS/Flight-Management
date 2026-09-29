package com.aerocadet.portal.auth;

import java.time.Instant;
import java.util.List;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UserSummary user) {

    public record UserSummary(
            Long id,
            String fullName,
            String email,
            List<String> roles) {
    }
}

