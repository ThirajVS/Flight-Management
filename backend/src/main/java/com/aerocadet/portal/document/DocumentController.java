package com.aerocadet.portal.document;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class DocumentController {
    private final DocumentService documentService;
    public DocumentController(DocumentService documentService) { this.documentService = documentService; }

    @PostMapping(value = "/applications/{applicationId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<DocumentDtos.Response> upload(Authentication authentication, @PathVariable Long applicationId,
            @RequestParam DocumentType type, @RequestPart("file") MultipartFile file) {
        return ResponseEntity.ok(documentService.upload(authentication.getName(), applicationId, type, file));
    }

    @GetMapping("/applications/{applicationId}/documents")
    public ResponseEntity<List<DocumentDtos.Response>> list(Authentication authentication, @PathVariable Long applicationId) {
        boolean reviewer = authentication.getAuthorities().stream().anyMatch(authority ->
                authority.getAuthority().equals("ROLE_RECRUITER") || authority.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(documentService.list(authentication.getName(), applicationId, reviewer));
    }

    @PutMapping("/documents/{documentId}/review")
    @PreAuthorize("hasAnyRole('RECRUITER','ADMIN')")
    public ResponseEntity<DocumentDtos.Response> review(Authentication authentication, @PathVariable Long documentId,
            @Valid @RequestBody DocumentDtos.ReviewRequest request) {
        return ResponseEntity.ok(documentService.review(authentication.getName(), documentId, request));
    }
}

