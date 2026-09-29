package com.aerocadet.portal.application;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.aerocadet.portal.audit.AuditService;
import com.aerocadet.portal.common.InvalidOperationException;
import com.aerocadet.portal.common.ResourceConflictException;
import com.aerocadet.portal.common.ResourceNotFoundException;
import com.aerocadet.portal.notification.NotificationService;
import com.aerocadet.portal.program.CadetProgram;
import com.aerocadet.portal.program.CadetProgramService;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationWorkflowService {

    private static final List<String> DEFAULT_STAGES = List.of(
            "Application", "Eligibility", "Aptitude Test", "Technical Assessment",
            "Interview", "Medical", "Final Selection");

    private final CadetApplicationRepository applicationRepository;
    private final ApplicationStatusHistoryRepository historyRepository;
    private final SelectionStageRepository stageRepository;
    private final UserAccountRepository userAccountRepository;
    private final CadetProgramService programService;
    private final NotificationService notificationService;
    private final AuditService auditService;
    private final ObjectMapper objectMapper;

    public ApplicationWorkflowService(
            CadetApplicationRepository applicationRepository,
            ApplicationStatusHistoryRepository historyRepository,
            SelectionStageRepository stageRepository,
            UserAccountRepository userAccountRepository,
            CadetProgramService programService,
            NotificationService notificationService,
            AuditService auditService,
            ObjectMapper objectMapper) {
        this.applicationRepository = applicationRepository;
        this.historyRepository = historyRepository;
        this.stageRepository = stageRepository;
        this.userAccountRepository = userAccountRepository;
        this.programService = programService;
        this.notificationService = notificationService;
        this.auditService = auditService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public ApplicationDtos.Response create(String email, ApplicationDtos.CreateRequest request) {
        if (applicationRepository.existsByCandidateEmailIgnoreCaseAndProgramId(email, request.programId())) {
            throw new ResourceConflictException("An application for this program already exists");
        }
        UserAccount candidate = user(email);
        CadetProgram program = programService.findEntity(request.programId());
        String number = "AC-" + java.time.Year.now().getValue() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        CadetApplication application = applicationRepository.save(new CadetApplication(number, candidate, program));
        historyRepository.save(new ApplicationStatusHistory(application, ApplicationStatus.DRAFT, candidate, "Application created"));
        auditService.record(email, "APPLICATION_CREATED", "APPLICATION", application.getId().toString(), number);
        return response(application);
    }

    @Transactional(readOnly = true)
    public List<ApplicationDtos.Response> listOwn(String email) {
        return applicationRepository.findByCandidateEmailIgnoreCaseOrderByUpdatedAtDesc(email).stream()
                .map(this::response).toList();
    }

    @Transactional
    public ApplicationDtos.Response saveDraft(String email, Long id, ApplicationDtos.DraftRequest request) {
        CadetApplication application = owned(email, id);
        if (application.getStatus() != ApplicationStatus.DRAFT) {
            throw new InvalidOperationException("Only draft applications can be edited");
        }
        try {
            application.saveDraft(request.step(), objectMapper.writeValueAsString(request.data()));
            auditService.record(email, "APPLICATION_DRAFT_SAVED", "APPLICATION", id.toString(), "Step " + request.step());
            return response(application);
        } catch (JsonProcessingException exception) {
            throw new InvalidOperationException("Application data could not be saved");
        }
    }

    @Transactional
    public ApplicationDtos.Response submit(String email, Long id) {
        CadetApplication application = owned(email, id);
        if (application.getStatus() != ApplicationStatus.DRAFT) {
            throw new InvalidOperationException("Only draft applications can be submitted");
        }
        application.submit();
        UserAccount candidate = application.getCandidate();
        historyRepository.save(new ApplicationStatusHistory(application, ApplicationStatus.SUBMITTED, candidate, "Application submitted by candidate"));
        if (!stageRepository.existsByApplicationId(id)) {
            for (int index = 0; index < DEFAULT_STAGES.size(); index++) {
                stageRepository.save(new SelectionStage(application, index + 1, DEFAULT_STAGES.get(index), index == 0 ? "COMPLETED" : "NOT_STARTED"));
            }
        }
        notificationService.create(candidate, "APPLICATION", "Application submitted",
                "Your application " + application.getApplicationNumber() + " has been submitted.");
        auditService.record(email, "APPLICATION_SUBMITTED", "APPLICATION", id.toString(), application.getApplicationNumber());
        return response(application);
    }

    @Transactional
    public ApplicationDtos.Response updateStatus(String actorEmail, Long id, ApplicationDtos.StatusUpdate update) {
        CadetApplication application = find(id);
        if (application.getStatus() == ApplicationStatus.DRAFT) {
            throw new InvalidOperationException("A draft application must be submitted before review");
        }
        UserAccount actor = user(actorEmail);
        application.changeStatus(update.status());
        historyRepository.save(new ApplicationStatusHistory(application, update.status(), actor, update.remarks()));
        notificationService.create(application.getCandidate(), "STATUS_CHANGE", "Application status updated",
                application.getApplicationNumber() + " is now " + update.status().name().replace('_', ' ') + ".");
        auditService.record(actorEmail, "APPLICATION_STATUS_CHANGED", "APPLICATION", id.toString(),
                update.status().name() + (update.remarks() == null ? "" : ": " + update.remarks()));
        return response(application);
    }

    @Transactional
    public ApplicationDtos.StageItem updateStage(String actorEmail, Long applicationId, Long stageId, ApplicationDtos.StageUpdate update) {
        CadetApplication application = find(applicationId);
        SelectionStage stage = stageRepository.findById(stageId)
                .filter(item -> item.getApplication().getId().equals(applicationId))
                .orElseThrow(() -> new ResourceNotFoundException("Selection stage was not found"));
        stage.update(update.status(), update.scheduledAt(), update.score(), update.remarks());
        notificationService.create(application.getCandidate(), "SELECTION_STAGE", "Selection stage updated",
                stage.getStage() + " is now " + update.status().replace('_', ' ') + ".");
        auditService.record(actorEmail, "SELECTION_STAGE_UPDATED", "APPLICATION", applicationId.toString(),
                stage.getStage() + ": " + update.status());
        return new ApplicationDtos.StageItem(stage.getId(), stage.getStageOrder(), stage.getStage(), stage.getStatus(),
                stage.getScheduledAt(), stage.getScore(), stage.getRemarks());
    }

    @Transactional(readOnly = true)
    public ApplicationDtos.Tracking tracking(String email, Long id, boolean reviewer) {
        CadetApplication application = reviewer ? find(id) : owned(email, id);
        List<ApplicationDtos.HistoryItem> history = historyRepository.findByApplicationIdOrderByCreatedAtAsc(id).stream()
                .map(item -> new ApplicationDtos.HistoryItem(
                        item.getStatus(), item.getChangedBy().getFullName(), item.getRemarks(), item.getCreatedAt()))
                .toList();
        List<ApplicationDtos.StageItem> stages = stageRepository.findByApplicationIdOrderByStageOrderAsc(id).stream()
                .map(item -> new ApplicationDtos.StageItem(
                        item.getId(), item.getStageOrder(), item.getStage(), item.getStatus(),
                        item.getScheduledAt(), item.getScore(), item.getRemarks()))
                .toList();
        return new ApplicationDtos.Tracking(response(application), history, stages);
    }

    public CadetApplication owned(String email, Long id) {
        CadetApplication application = find(id);
        if (!application.getCandidate().getEmail().equalsIgnoreCase(email)) {
            throw new ResourceNotFoundException("Application was not found");
        }
        return application;
    }

    public CadetApplication find(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application was not found"));
    }

    private UserAccount user(String email) {
        return userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User account was not found"));
    }

    private ApplicationDtos.Response response(CadetApplication application) {
        try {
            Map<String, Object> draft = objectMapper.readValue(application.getDraftDataJson(), new TypeReference<>() {});
            return new ApplicationDtos.Response(
                    application.getId(), application.getApplicationNumber(), application.getProgram().getId(),
                    application.getProgram().getName(), application.getStatus(), application.getCurrentStep(),
                    draft, application.getSubmittedAt(), application.getCreatedAt(), application.getUpdatedAt());
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Stored application data is invalid", exception);
        }
    }
}

