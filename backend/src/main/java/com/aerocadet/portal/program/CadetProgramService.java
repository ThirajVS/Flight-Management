package com.aerocadet.portal.program;

import java.util.Locale;

import com.aerocadet.portal.common.ResourceNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadetProgramService {

    private final CadetProgramRepository cadetProgramRepository;

    public CadetProgramService(CadetProgramRepository cadetProgramRepository) {
        this.cadetProgramRepository = cadetProgramRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProgramResponse> search(
            String search,
            String location,
            ProgramStatus status,
            int page,
            int size) {
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(Math.max(page, 0), safeSize, Sort.by("applicationDeadline").ascending());
        Specification<CadetProgram> specification = (root, query, builder) -> {
            Predicate predicate = builder.conjunction();
            if (search != null && !search.isBlank()) {
                String pattern = "%" + search.trim().toLowerCase(Locale.ROOT) + "%";
                predicate = builder.and(predicate, builder.or(
                        builder.like(builder.lower(root.get("name")), pattern),
                        builder.like(builder.lower(root.get("organisation")), pattern),
                        builder.like(builder.lower(root.get("code")), pattern)));
            }
            if (location != null && !location.isBlank()) {
                predicate = builder.and(predicate,
                        builder.like(builder.lower(root.get("trainingLocation")),
                                "%" + location.trim().toLowerCase(Locale.ROOT) + "%"));
            }
            if (status != null) {
                predicate = builder.and(predicate, builder.equal(root.get("status"), status));
            }
            return predicate;
        };
        return cadetProgramRepository.findAll(specification, pageable).map(ProgramResponse::from);
    }

    @Transactional(readOnly = true)
    public ProgramResponse get(Long id) {
        return ProgramResponse.from(findEntity(id));
    }

    public CadetProgram findEntity(Long id) {
        return cadetProgramRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cadet program was not found"));
    }
}

