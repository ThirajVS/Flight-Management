package com.aerocadet.portal.user;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CandidateProfileUpdate(
        @NotBlank @Pattern(regexp = "^[+]?[0-9 ()-]{7,20}$") String phone,
        @NotBlank @Size(max = 80) String nationality,
        @NotBlank @Size(max = 80) String city,
        @NotBlank @Size(max = 80) String state,
        @NotBlank @Size(max = 80) String country,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal tenthPercentage,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal twelfthPercentage,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal physicsMarks,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal mathematicsMarks,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal englishMarks,
        @Size(max = 250) String graduationDetails,
        @NotBlank @Pattern(regexp = "NOT_SUBMITTED|DOCUMENT_UPLOADED|PENDING_VERIFICATION|VERIFIED|REQUIRES_UPDATE") String medicalStatus,
        @Size(max = 250) String flyingExperience,
        @Min(0) Integer totalFlightHours,
        @Size(max = 250) String previousAviationTraining,
        @NotBlank @Pattern(regexp = "BASIC|INTERMEDIATE|ADVANCED|ICAO_LEVEL_4_OR_HIGHER") String englishProficiency,
        @NotNull Boolean passportAvailable,
        @Size(max = 120) String preferredProgram,
        @Size(max = 120) String preferredTrainingLocation,
        @Size(max = 120) String preferredAirline,
        LocalDate availableFrom) {
}

