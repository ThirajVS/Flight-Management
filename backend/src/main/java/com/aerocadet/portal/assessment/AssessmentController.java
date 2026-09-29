package com.aerocadet.portal.assessment;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assessments")
@PreAuthorize("hasRole('CANDIDATE')")
public class AssessmentController {
    private final AssessmentService assessmentService;
    public AssessmentController(AssessmentService assessmentService) { this.assessmentService = assessmentService; }
    @GetMapping("/questions") public ResponseEntity<List<AssessmentService.QuestionResponse>> questions() { return ResponseEntity.ok(assessmentService.questions()); }
    @PostMapping("/submit") public ResponseEntity<AssessmentService.ResultResponse> submit(Authentication authentication,
            @Valid @RequestBody AssessmentService.Submission submission) {
        return ResponseEntity.ok(assessmentService.submit(authentication.getName(), submission));
    }
}

