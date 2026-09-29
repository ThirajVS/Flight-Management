package com.aerocadet.portal.document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.aerocadet.portal.application.ApplicationWorkflowService;
import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.audit.AuditService;
import com.aerocadet.portal.common.InvalidOperationException;
import com.aerocadet.portal.common.ResourceNotFoundException;
import com.aerocadet.portal.notification.NotificationService;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DocumentService {

    private static final Set<String> ALLOWED_TYPES = Set.of("application/pdf", "image/jpeg", "image/png");

    private final Path uploadDirectory;
    private final ApplicationDocumentRepository documentRepository;
    private final ApplicationWorkflowService workflowService;
    private final UserAccountRepository userAccountRepository;
    private final NotificationService notificationService;
    private final AuditService auditService;

    public DocumentService(
            @Value("${app.storage.upload-directory}") String uploadDirectory,
            ApplicationDocumentRepository documentRepository,
            ApplicationWorkflowService workflowService,
            UserAccountRepository userAccountRepository,
            NotificationService notificationService,
            AuditService auditService) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
        this.documentRepository = documentRepository;
        this.workflowService = workflowService;
        this.userAccountRepository = userAccountRepository;
        this.notificationService = notificationService;
        this.auditService = auditService;
    }

    @Transactional
    public DocumentDtos.Response upload(String email, Long applicationId, DocumentType type, MultipartFile file) {
        CadetApplication application = workflowService.owned(email, applicationId);
        validate(file);
        String original = Path.of(file.getOriginalFilename() == null ? "document" : file.getOriginalFilename()).getFileName().toString();
        String extension = original.contains(".") ? original.substring(original.lastIndexOf('.')).toLowerCase() : "";
        String stored = application.getApplicationNumber() + "-" + UUID.randomUUID() + extension;
        Path target = uploadDirectory.resolve(stored).normalize();
        if (!target.startsWith(uploadDirectory)) {
            throw new InvalidOperationException("Invalid document filename");
        }
        try {
            Files.createDirectories(uploadDirectory);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Document could not be stored", exception);
        }
        ApplicationDocument document = documentRepository.save(new ApplicationDocument(
                application, type, original, stored, file.getContentType(), file.getSize()));
        auditService.record(email, "DOCUMENT_UPLOADED", "DOCUMENT", document.getId().toString(), type.name());
        return DocumentDtos.Response.from(document);
    }

    @Transactional(readOnly = true)
    public List<DocumentDtos.Response> list(String email, Long applicationId, boolean reviewer) {
        if (!reviewer) { workflowService.owned(email, applicationId); }
        else { workflowService.find(applicationId); }
        return documentRepository.findByApplicationIdOrderByUploadedAtDesc(applicationId).stream()
                .map(DocumentDtos.Response::from).toList();
    }

    @Transactional
    public DocumentDtos.Response review(String actorEmail, Long documentId, DocumentDtos.ReviewRequest request) {
        if (request.status() != DocumentStatus.VERIFIED && request.status() != DocumentStatus.REJECTED
                && request.status() != DocumentStatus.REUPLOAD_REQUESTED) {
            throw new InvalidOperationException("Review status must be VERIFIED, REJECTED, or REUPLOAD_REQUESTED");
        }
        ApplicationDocument document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document was not found"));
        UserAccount reviewer = userAccountRepository.findByEmailIgnoreCase(actorEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer account was not found"));
        document.review(request.status(), request.reason(), reviewer);
        notificationService.create(document.getApplication().getCandidate(), "DOCUMENT", "Document review updated",
                document.getDocumentType().name().replace('_', ' ') + " is now " + request.status().name().replace('_', ' ') + ".");
        auditService.record(actorEmail, "DOCUMENT_REVIEWED", "DOCUMENT", documentId.toString(), request.status().name());
        return DocumentDtos.Response.from(document);
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) { throw new InvalidOperationException("Document file is empty"); }
        if (file.getSize() > 10 * 1024 * 1024) { throw new InvalidOperationException("Document exceeds the 10 MB limit"); }
        if (file.getContentType() == null || !ALLOWED_TYPES.contains(file.getContentType())) {
            throw new InvalidOperationException("Only PDF, JPEG, and PNG documents are accepted");
        }
    }
}

