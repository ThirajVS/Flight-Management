package com.aerocadet.portal.eligibility;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/programs/{programId}/eligibility")
@PreAuthorize("hasRole('CANDIDATE')")
public class EligibilityController {

    private final EligibilityService eligibilityService;

    public EligibilityController(EligibilityService eligibilityService) {
        this.eligibilityService = eligibilityService;
    }

    @PostMapping
    public ResponseEntity<EligibilityResponse> evaluate(
            @PathVariable Long programId,
            Authentication authentication) {
        return ResponseEntity.ok(eligibilityService.evaluate(authentication.getName(), programId));
    }

    @GetMapping("/latest")
    public ResponseEntity<EligibilityResponse> latest(
            @PathVariable Long programId,
            Authentication authentication) {
        return ResponseEntity.ok(eligibilityService.latest(authentication.getName(), programId));
    }
}

