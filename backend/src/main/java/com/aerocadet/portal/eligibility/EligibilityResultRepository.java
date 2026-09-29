package com.aerocadet.portal.eligibility;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EligibilityResultRepository extends JpaRepository<EligibilityResult, Long> {
    Optional<EligibilityResult> findTopByCandidateEmailIgnoreCaseAndProgramIdOrderByCheckedAtDesc(String email, Long programId);
}

