package com.aerocadet.portal.eligibility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.aerocadet.portal.common.ResourceNotFoundException;
import com.aerocadet.portal.program.CadetProgram;
import com.aerocadet.portal.program.CadetProgramService;
import com.aerocadet.portal.user.CandidateProfile;
import com.aerocadet.portal.user.CandidateProfileRepository;
import com.aerocadet.portal.user.UserAccount;
import com.aerocadet.portal.user.UserAccountRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EligibilityService {

    private final CandidateProfileRepository candidateProfileRepository;
    private final UserAccountRepository userAccountRepository;
    private final CadetProgramService cadetProgramService;
    private final EligibilityResultRepository eligibilityResultRepository;
    private final ObjectMapper objectMapper;

    public EligibilityService(
            CandidateProfileRepository candidateProfileRepository,
            UserAccountRepository userAccountRepository,
            CadetProgramService cadetProgramService,
            EligibilityResultRepository eligibilityResultRepository,
            ObjectMapper objectMapper) {
        this.candidateProfileRepository = candidateProfileRepository;
        this.userAccountRepository = userAccountRepository;
        this.cadetProgramService = cadetProgramService;
        this.eligibilityResultRepository = eligibilityResultRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public EligibilityResponse evaluate(String email, Long programId) {
        CandidateProfile profile = candidateProfileRepository.findByUserEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate profile was not found"));
        UserAccount user = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate account was not found"));
        CadetProgram program = cadetProgramService.findEntity(programId);

        List<EligibilityResponse.EligibilityCheck> checks = buildChecks(profile, program);
        EligibilityStatus overall = overall(checks);
        try {
            EligibilityResult result = eligibilityResultRepository.save(
                    new EligibilityResult(user, program, overall, objectMapper.writeValueAsString(checks)));
            return response(result, checks);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not store eligibility result", exception);
        }
    }

    @Transactional(readOnly = true)
    public EligibilityResponse latest(String email, Long programId) {
        EligibilityResult result = eligibilityResultRepository
                .findTopByCandidateEmailIgnoreCaseAndProgramIdOrderByCheckedAtDesc(email, programId)
                .orElseThrow(() -> new ResourceNotFoundException("No eligibility result exists for this program"));
        try {
            List<EligibilityResponse.EligibilityCheck> checks = objectMapper.readValue(
                    result.getChecksJson(),
                    new TypeReference<>() {});
            return response(result, checks);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Could not read eligibility result", exception);
        }
    }

    private List<EligibilityResponse.EligibilityCheck> buildChecks(CandidateProfile profile, CadetProgram program) {
        List<EligibilityResponse.EligibilityCheck> checks = new ArrayList<>();
        int age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
        checks.add(check("Age requirement", age >= program.getMinAge() && age <= program.getMaxAge(),
                "Age %d; required %d–%d".formatted(age, program.getMinAge(), program.getMaxAge())));
        checks.add(markCheck("12th academic requirement", profile.getTwelfthPercentage(), program.getMinTwelfthPercentage()));
        checks.add(markCheck("Physics", profile.getPhysicsMarks(), program.getMinPhysicsMarks()));
        checks.add(markCheck("Mathematics", profile.getMathematicsMarks(), program.getMinMathematicsMarks()));
        checks.add(markCheck("English", profile.getEnglishMarks(), program.getMinEnglishMarks()));

        if (program.isMedicalRequired()) {
            CheckStatus status = "VERIFIED".equals(profile.getMedicalStatus()) ? CheckStatus.PASSED : CheckStatus.PENDING;
            checks.add(new EligibilityResponse.EligibilityCheck(
                    "Medical verification",
                    status,
                    status == CheckStatus.PASSED ? "Administrative document verified" : "Verification is pending; no medical fitness decision is made"));
        }
        if (program.isPassportRequired()) {
            checks.add(check("Passport", Boolean.TRUE.equals(profile.getPassportAvailable()),
                    Boolean.TRUE.equals(profile.getPassportAvailable()) ? "Passport recorded" : "Passport is required"));
        }
        return checks;
    }

    private EligibilityResponse.EligibilityCheck markCheck(String criterion, BigDecimal actual, BigDecimal required) {
        if (actual == null) {
            return new EligibilityResponse.EligibilityCheck(criterion, CheckStatus.PENDING, "Profile information is incomplete");
        }
        return check(criterion, actual.compareTo(required) >= 0,
                "%.2f%% recorded; %.2f%% required".formatted(actual, required));
    }

    private EligibilityResponse.EligibilityCheck check(String criterion, boolean passed, String message) {
        return new EligibilityResponse.EligibilityCheck(
                criterion,
                passed ? CheckStatus.PASSED : CheckStatus.FAILED,
                message);
    }

    private EligibilityStatus overall(List<EligibilityResponse.EligibilityCheck> checks) {
        if (checks.stream().anyMatch(check -> check.status() == CheckStatus.FAILED)) {
            return EligibilityStatus.NOT_ELIGIBLE;
        }
        if (checks.stream().anyMatch(check -> check.status() == CheckStatus.PENDING)) {
            return EligibilityStatus.PENDING_VERIFICATION;
        }
        return EligibilityStatus.ELIGIBLE;
    }

    private EligibilityResponse response(
            EligibilityResult result,
            List<EligibilityResponse.EligibilityCheck> checks) {
        return new EligibilityResponse(
                result.getId(),
                result.getProgram().getId(),
                result.getProgram().getName(),
                result.getStatus(),
                checks,
                result.getCheckedAt());
    }
}

