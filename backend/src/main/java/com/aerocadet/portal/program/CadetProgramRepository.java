package com.aerocadet.portal.program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CadetProgramRepository extends JpaRepository<CadetProgram, Long>, JpaSpecificationExecutor<CadetProgram> {
    Page<CadetProgram> findByStatus(ProgramStatus status, Pageable pageable);
}

