package com.aerocadet.portal.assessment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import com.aerocadet.portal.application.ApplicationWorkflowService;
import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.audit.AuditService;
import com.aerocadet.portal.notification.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssessmentService {
    private final AssessmentQuestionRepository questionRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final ApplicationWorkflowService workflowService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;
    public AssessmentService(AssessmentQuestionRepository questionRepository, AssessmentAttemptRepository attemptRepository,
            ApplicationWorkflowService workflowService, NotificationService notificationService,
            AuditService auditService, ObjectMapper objectMapper) {
        this.questionRepository=questionRepository; this.attemptRepository=attemptRepository; this.workflowService=workflowService;
        this.notificationService=notificationService; this.auditService=auditService; this.objectMapper=objectMapper;
    }
    @Transactional(readOnly = true)
    public List<QuestionResponse> questions() {
        return questionRepository.findByActiveTrueOrderByIdAsc().stream()
                .map(q -> new QuestionResponse(q.getId(), q.getCategory(), q.getQuestion(),
                        Map.of("A",q.getOptionA(),"B",q.getOptionB(),"C",q.getOptionC(),"D",q.getOptionD()), q.getPoints()))
                .toList();
    }
    @Transactional
    public ResultResponse submit(String email, Submission submission) {
        CadetApplication application = workflowService.owned(email, submission.applicationId());
        List<AssessmentQuestion> questions = questionRepository.findByActiveTrueOrderByIdAsc();
        int total = questions.stream().mapToInt(AssessmentQuestion::getPoints).sum();
        int earned = questions.stream().filter(q -> q.getCorrectOption().equalsIgnoreCase(submission.answers().get(q.getId())))
                .mapToInt(AssessmentQuestion::getPoints).sum();
        BigDecimal score = total == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(earned * 100.0 / total).setScale(2, RoundingMode.HALF_UP);
        String status = score.compareTo(BigDecimal.valueOf(70)) >= 0 ? "QUALIFIED" : "NOT_QUALIFIED";
        try {
            AssessmentAttempt attempt = attemptRepository.save(new AssessmentAttempt(application, application.getCandidate(),
                    submission.startedAt(), score, status, objectMapper.writeValueAsString(submission.answers())));
            notificationService.create(application.getCandidate(), "ASSESSMENT", "Assessment result available",
                    "Score: " + score + "/100 — " + status.replace('_',' '));
            auditService.record(email, "ASSESSMENT_SUBMITTED", "ASSESSMENT", attempt.getId().toString(), "Score " + score);
            return new ResultResponse(attempt.getId(), score, status, attempt.getSubmittedAt());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Assessment answers could not be stored", exception);
        }
    }
    public record QuestionResponse(Long id, String category, String question, Map<String,String> options, int points) {}
    public record Submission(@jakarta.validation.constraints.NotNull Long applicationId,
                             @jakarta.validation.constraints.NotNull Instant startedAt,
                             @jakarta.validation.constraints.NotNull Map<Long,String> answers) {}
    public record ResultResponse(Long attemptId, BigDecimal score, String status, Instant submittedAt) {}
}

