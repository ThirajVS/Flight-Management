package com.aerocadet.portal.interview;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService service;
    public InterviewController(InterviewService service){this.service=service;}
    @PostMapping @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<InterviewService.Response> create(Authentication auth,@Valid @RequestBody InterviewService.Request request){return ResponseEntity.ok(service.create(auth.getName(),request));}
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<InterviewService.Response> update(Authentication auth,@PathVariable Long id,@Valid @RequestBody InterviewService.Request request){return ResponseEntity.ok(service.update(auth.getName(),id,request));}
    @GetMapping @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<InterviewService.Response>> candidate(Authentication auth){return ResponseEntity.ok(service.candidate(auth.getName()));}
}

