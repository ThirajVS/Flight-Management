package com.aerocadet.portal.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CandidateProfileResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        LocalDate dateOfBirth,
        String nationality,
        String city,
        String state,
        String country,
        int profileCompletion,
        BigDecimal tenthPercentage,
        BigDecimal twelfthPercentage,
        BigDecimal physicsMarks,
        BigDecimal mathematicsMarks,
        BigDecimal englishMarks,
        String graduationDetails,
        String medicalStatus,
        String flyingExperience,
        Integer totalFlightHours,
        String previousAviationTraining,
        String englishProficiency,
        Boolean passportAvailable,
        String preferredProgram,
        String preferredTrainingLocation,
        String preferredAirline,
        LocalDate availableFrom) {

    static CandidateProfileResponse from(CandidateProfile profile) {
        return new CandidateProfileResponse(
                profile.getId(),
                profile.getUser().getFullName(),
                profile.getUser().getEmail(),
                profile.getPhone(),
                profile.getDateOfBirth(),
                profile.getNationality(),
                profile.getCity(),
                profile.getState(),
                profile.getCountry(),
                profile.getProfileCompletion(),
                profile.getTenthPercentage(),
                profile.getTwelfthPercentage(),
                profile.getPhysicsMarks(),
                profile.getMathematicsMarks(),
                profile.getEnglishMarks(),
                profile.getGraduationDetails(),
                profile.getMedicalStatus(),
                profile.getFlyingExperience(),
                profile.getTotalFlightHours(),
                profile.getPreviousAviationTraining(),
                profile.getEnglishProficiency(),
                profile.getPassportAvailable(),
                profile.getPreferredProgram(),
                profile.getPreferredTrainingLocation(),
                profile.getPreferredAirline(),
                profile.getAvailableFrom());
    }
}

