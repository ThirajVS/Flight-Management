package com.aerocadet.portal.program;

import java.util.List;

import org.springframework.data.domain.Page;

public record ProgramPageResponse(
        List<ProgramResponse> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last) {

    static ProgramPageResponse from(Page<ProgramResponse> result) {
        return new ProgramPageResponse(
                result.getContent(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isFirst(),
                result.isLast());
    }
}

