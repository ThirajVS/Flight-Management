package com.aerocadet.portal.program;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cadet_programs")
public class CadetProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String code;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(nullable = false, length = 160)
    private String organisation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "training_location", nullable = false, length = 160)
    private String trainingLocation;

    @Column(name = "duration_months", nullable = false)
    private int durationMonths;

    @Column(name = "estimated_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal estimatedCost;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "max_age", nullable = false)
    private int maxAge;

    @Column(name = "min_twelfth_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal minTwelfthPercentage;

    @Column(name = "min_physics_marks", nullable = false, precision = 5, scale = 2)
    private BigDecimal minPhysicsMarks;

    @Column(name = "min_mathematics_marks", nullable = false, precision = 5, scale = 2)
    private BigDecimal minMathematicsMarks;

    @Column(name = "min_english_marks", nullable = false, precision = 5, scale = 2)
    private BigDecimal minEnglishMarks;

    @Column(name = "medical_required", nullable = false)
    private boolean medicalRequired;

    @Column(name = "passport_required", nullable = false)
    private boolean passportRequired;

    @Column(name = "application_deadline", nullable = false)
    private LocalDate applicationDeadline;

    @Column(name = "selection_stages", nullable = false, columnDefinition = "TEXT")
    private String selectionStages;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgramStatus status;

    protected CadetProgram() {
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getOrganisation() { return organisation; }
    public String getDescription() { return description; }
    public String getTrainingLocation() { return trainingLocation; }
    public int getDurationMonths() { return durationMonths; }
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public String getCurrency() { return currency; }
    public int getMinAge() { return minAge; }
    public int getMaxAge() { return maxAge; }
    public BigDecimal getMinTwelfthPercentage() { return minTwelfthPercentage; }
    public BigDecimal getMinPhysicsMarks() { return minPhysicsMarks; }
    public BigDecimal getMinMathematicsMarks() { return minMathematicsMarks; }
    public BigDecimal getMinEnglishMarks() { return minEnglishMarks; }
    public boolean isMedicalRequired() { return medicalRequired; }
    public boolean isPassportRequired() { return passportRequired; }
    public LocalDate getApplicationDeadline() { return applicationDeadline; }
    public String getSelectionStages() { return selectionStages; }
    public ProgramStatus getStatus() { return status; }
}

