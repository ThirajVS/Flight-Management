package com.aerocadet.portal.interview;

import java.time.Instant;
import java.util.List;

import com.aerocadet.portal.application.ApplicationWorkflowService;
import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.audit.AuditService;
import com.aerocadet.portal.common.ResourceNotFoundException;
import com.aerocadet.portal.notification.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewService {
    private final InterviewRepository repository;
    private final ApplicationWorkflowService workflowService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    public InterviewService(InterviewRepository repository, ApplicationWorkflowService workflowService,
            NotificationService notificationService, AuditService auditService) {
        this.repository=repository; this.workflowService=workflowService; this.notificationService=notificationService; this.auditService=auditService;
    }
    @Transactional
    public Response create(String actor, Request request) {
        CadetApplication application = workflowService.find(request.applicationId());
        Interview interview = repository.save(new Interview(application, request.scheduledAt(), request.mode(), request.location(), request.interviewer(), request.remarks()));
        notificationService.create(application.getCandidate(), "INTERVIEW", "Interview scheduled",
                "Your interview is scheduled for " + request.scheduledAt() + ".");
        auditService.record(actor, "INTERVIEW_SCHEDULED", "INTERVIEW", interview.getId().toString(), request.mode());
        return Response.from(interview);
    }
    @Transactional
    public Response update(String actor, Long id, Request request) {
        Interview interview = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Interview was not found"));
        interview.update(request.scheduledAt(), request.mode(), request.location(), request.interviewer(), request.remarks(), request.status());
        notificationService.create(interview.getCandidate(), "INTERVIEW", "Interview updated", "Interview status: " + request.status());
        auditService.record(actor, "INTERVIEW_UPDATED", "INTERVIEW", id.toString(), request.status());
        return Response.from(interview);
    }
    @Transactional(readOnly = true)
    public List<Response> candidate(String email) { return repository.findByCandidateEmailIgnoreCaseOrderByScheduledAtAsc(email).stream().map(Response::from).toList(); }
    public record Request(@jakarta.validation.constraints.NotNull Long applicationId,
                          @jakarta.validation.constraints.NotNull Instant scheduledAt,
                          @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Pattern(regexp="IN_PERSON|VIDEO|TELEPHONE") String mode,
                          @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max=300) String location,
                          @jakarta.validation.constraints.NotBlank @jakarta.validation.constraints.Size(max=120) String interviewer,
                          @jakarta.validation.constraints.Size(max=1000) String remarks,
                          @jakarta.validation.constraints.Pattern(regexp="SCHEDULED|COMPLETED|CANCELLED|RESCHEDULED") String status) {}
    public record Response(Long id, Long applicationId, String applicationNumber, Instant scheduledAt, String mode,
                           String location, String interviewer, String remarks, String status) {
        static Response from(Interview i){return new Response(i.getId(),i.getApplication().getId(),i.getApplication().getApplicationNumber(),i.getScheduledAt(),i.getMode(),i.getLocation(),i.getInterviewer(),i.getRemarks(),i.getStatus());}
    }
}

