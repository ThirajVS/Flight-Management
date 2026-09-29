package com.aerocadet.portal.user;

import com.aerocadet.portal.common.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CandidateProfileService {

    private final CandidateProfileRepository candidateProfileRepository;

    public CandidateProfileService(CandidateProfileRepository candidateProfileRepository) {
        this.candidateProfileRepository = candidateProfileRepository;
    }

    @Transactional(readOnly = true)
    public CandidateProfileResponse get(String email) {
        return CandidateProfileResponse.from(find(email));
    }

    @Transactional
    public CandidateProfileResponse update(String email, CandidateProfileUpdate update) {
        CandidateProfile profile = find(email);
        profile.updateDetails(update);
        return CandidateProfileResponse.from(profile);
    }

    CandidateProfile find(String email) {
        return candidateProfileRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile was not found"));
    }
}

