package com.aerocadet.portal.application;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SelectionStageRepository extends JpaRepository<SelectionStage, Long> {
    List<SelectionStage> findByApplicationIdOrderByStageOrderAsc(Long applicationId);
    boolean existsByApplicationId(Long applicationId);
}

