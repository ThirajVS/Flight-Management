package com.aerocadet.portal.document;

import java.time.Instant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class DocumentDtos {
    private DocumentDtos() {}
    public record ReviewRequest(@NotNull DocumentStatus status, @Size(max = 1000) String reason) {}
    public record Response(Long id, Long applicationId, DocumentType type, String originalFilename,
                           String contentType, long fileSize, DocumentStatus status,
                           String reviewReason, Instant uploadedAt, Instant reviewedAt) {
        static Response from(ApplicationDocument document) {
            return new Response(document.getId(), document.getApplication().getId(), document.getDocumentType(),
                    document.getOriginalFilename(), document.getContentType(), document.getFileSize(),
                    document.getStatus(), document.getReviewReason(), document.getUploadedAt(), document.getReviewedAt());
        }
    }
}

