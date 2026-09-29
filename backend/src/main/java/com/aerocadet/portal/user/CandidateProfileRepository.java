package com.aerocadet.portal.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long> {
    Optional<CandidateProfile> findByUserEmailIgnoreCase(String email);
}

