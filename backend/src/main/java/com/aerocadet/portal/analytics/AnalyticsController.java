package com.aerocadet.portal.analytics;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnalyticsController {
    private final AnalyticsService service;
    public AnalyticsController(AnalyticsService service){this.service=service;}
    @GetMapping("/api/analytics/summary") @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AnalyticsService.Summary> summary(){return ResponseEntity.ok(service.summary());}
    @GetMapping("/api/recruiter/dashboard") @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<AnalyticsService.RecruiterSummary> recruiter(){return ResponseEntity.ok(service.recruiter());}
}

