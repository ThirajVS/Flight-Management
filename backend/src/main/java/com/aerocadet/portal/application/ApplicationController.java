package com.aerocadet.portal.application;

import java.net.URI;
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
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationWorkflowService workflowService;

    public ApplicationController(ApplicationWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationDtos.Response> create(Authentication authentication, @Valid @RequestBody ApplicationDtos.CreateRequest request) {
        ApplicationDtos.Response response = workflowService.create(authentication.getName(), request);
        return ResponseEntity.created(URI.create("/api/applications/" + response.id())).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<List<ApplicationDtos.Response>> list(Authentication authentication) {
        return ResponseEntity.ok(workflowService.listOwn(authentication.getName()));
    }

    @PutMapping("/{id}/draft")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationDtos.Response> saveDraft(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ApplicationDtos.DraftRequest request) {
        return ResponseEntity.ok(workflowService.saveDraft(authentication.getName(), id, request));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<ApplicationDtos.Response> submit(Authentication authentication, @PathVariable Long id) {
        return ResponseEntity.ok(workflowService.submit(authentication.getName(), id));
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApplicationDtos.Tracking> tracking(Authentication authentication, @PathVariable Long id) {
        boolean reviewer = authentication.getAuthorities().stream().anyMatch(authority ->
                authority.getAuthority().equals("ROLE_RECRUITER") || authority.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(workflowService.tracking(authentication.getName(), id, reviewer));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<ApplicationDtos.Response> updateStatus(Authentication authentication, @PathVariable Long id, @Valid @RequestBody ApplicationDtos.StatusUpdate update) {
        return ResponseEntity.ok(workflowService.updateStatus(authentication.getName(), id, update));
    }

    @PutMapping("/{id}/stages/{stageId}")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<ApplicationDtos.StageItem> updateStage(Authentication authentication, @PathVariable Long id,
            @PathVariable Long stageId, @Valid @RequestBody ApplicationDtos.StageUpdate update) {
        return ResponseEntity.ok(workflowService.updateStage(authentication.getName(), id, stageId, update));
    }
}

