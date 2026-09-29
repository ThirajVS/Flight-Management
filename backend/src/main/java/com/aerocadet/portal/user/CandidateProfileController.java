package com.aerocadet.portal.user;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidates/profile")
@PreAuthorize("hasRole('CANDIDATE')")
public class CandidateProfileController {

    private final CandidateProfileService candidateProfileService;

    public CandidateProfileController(CandidateProfileService candidateProfileService) {
        this.candidateProfileService = candidateProfileService;
    }

    @GetMapping
    public ResponseEntity<CandidateProfileResponse> get(Authentication authentication) {
        return ResponseEntity.ok(candidateProfileService.get(authentication.getName()));
    }

    @PutMapping
    public ResponseEntity<CandidateProfileResponse> update(
            Authentication authentication,
            @Valid @RequestBody CandidateProfileUpdate update) {
        return ResponseEntity.ok(candidateProfileService.update(authentication.getName(), update));
    }
}

