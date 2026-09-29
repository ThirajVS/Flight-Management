package com.aerocadet.portal.interview;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewRepository extends JpaRepository<Interview, Long> {
    List<Interview> findByCandidateEmailIgnoreCaseOrderByScheduledAtAsc(String email);
    long countByStatus(String status);
}

