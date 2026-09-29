package com.aerocadet.portal.program;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProgramResponse(
        Long id,
        String code,
        String name,
        String organisation,
        String description,
        String trainingLocation,
        int durationMonths,
        BigDecimal estimatedCost,
        String currency,
        int minAge,
        int maxAge,
        BigDecimal minTwelfthPercentage,
        BigDecimal minPhysicsMarks,
        BigDecimal minMathematicsMarks,
        BigDecimal minEnglishMarks,
        boolean medicalRequired,
        boolean passportRequired,
        LocalDate applicationDeadline,
        List<String> selectionStages,
        ProgramStatus status,
        boolean fictionalDemo) {

    static ProgramResponse from(CadetProgram program) {
        return new ProgramResponse(
                program.getId(),
                program.getCode(),
                program.getName(),
                program.getOrganisation(),
                program.getDescription(),
                program.getTrainingLocation(),
                program.getDurationMonths(),
                program.getEstimatedCost(),
                program.getCurrency(),
                program.getMinAge(),
                program.getMaxAge(),
                program.getMinTwelfthPercentage(),
                program.getMinPhysicsMarks(),
                program.getMinMathematicsMarks(),
                program.getMinEnglishMarks(),
                program.isMedicalRequired(),
                program.isPassportRequired(),
                program.getApplicationDeadline(),
                List.of(program.getSelectionStages().split("\\|")),
                program.getStatus(),
                true);
    }
}

