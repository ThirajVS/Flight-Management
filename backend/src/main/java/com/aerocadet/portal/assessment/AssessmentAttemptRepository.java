package com.aerocadet.portal.assessment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    List<AssessmentAttempt> findByCandidateEmailIgnoreCaseOrderBySubmittedAtDesc(String email);
}

