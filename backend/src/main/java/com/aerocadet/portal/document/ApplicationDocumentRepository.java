package com.aerocadet.portal.document;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {
    List<ApplicationDocument> findByApplicationIdOrderByUploadedAtDesc(Long applicationId);
    long countByStatus(DocumentStatus status);
}

