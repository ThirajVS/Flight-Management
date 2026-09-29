package com.aerocadet.portal.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CadetApplicationRepository extends JpaRepository<CadetApplication, Long> {
    boolean existsByCandidateEmailIgnoreCaseAndProgramId(String email, Long programId);
    List<CadetApplication> findByCandidateEmailIgnoreCaseOrderByUpdatedAtDesc(String email);
}

