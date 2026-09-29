package com.aerocadet.portal.document;

import java.time.Instant;

import com.aerocadet.portal.application.CadetApplication;
import com.aerocadet.portal.user.UserAccount;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class ApplicationDocument {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "application_id", nullable = false)
    private CadetApplication application;
    @Enumerated(EnumType.STRING) @Column(name = "document_type", nullable = false, length = 40)
    private DocumentType documentType;
    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;
    @Column(name = "stored_filename", nullable = false, unique = true, length = 255)
    private String storedFilename;
    @Column(name = "content_type", nullable = false, length = 120)
    private String contentType;
    @Column(name = "file_size", nullable = false)
    private long fileSize;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 40)
    private DocumentStatus status = DocumentStatus.PENDING_VERIFICATION;
    @Column(name = "review_reason", columnDefinition = "TEXT")
    private String reviewReason;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "reviewed_by")
    private UserAccount reviewedBy;
    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private Instant uploadedAt;
    @Column(name = "reviewed_at")
    private Instant reviewedAt;

    protected ApplicationDocument() {}
    public ApplicationDocument(CadetApplication application, DocumentType type, String original, String stored, String contentType, long size) {
        this.application = application; this.documentType = type; this.originalFilename = original;
        this.storedFilename = stored; this.contentType = contentType; this.fileSize = size;
    }
    @PrePersist void setUploadedAt() { uploadedAt = Instant.now(); }
    public void review(DocumentStatus status, String reason, UserAccount reviewer) {
        this.status = status; this.reviewReason = reason; this.reviewedBy = reviewer; this.reviewedAt = Instant.now();
    }
    public Long getId() { return id; }
    public CadetApplication getApplication() { return application; }
    public DocumentType getDocumentType() { return documentType; }
    public String getOriginalFilename() { return originalFilename; }
    public String getStoredFilename() { return storedFilename; }
    public String getContentType() { return contentType; }
    public long getFileSize() { return fileSize; }
    public DocumentStatus getStatus() { return status; }
    public String getReviewReason() { return reviewReason; }
    public Instant getUploadedAt() { return uploadedAt; }
    public Instant getReviewedAt() { return reviewedAt; }
}

