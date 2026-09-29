package com.aerocadet.portal.assessment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {
    List<AssessmentQuestion> findByActiveTrueOrderByIdAsc();
}

