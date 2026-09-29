package com.aerocadet.portal.user;

import java.time.LocalDate;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidate_profiles")
public class CandidateProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserAccount user;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, length = 80)
    private String nationality;

    @Column(nullable = false, length = 80)
    private String city;

    @Column(nullable = false, length = 80)
    private String state;

    @Column(nullable = false, length = 80)
    private String country;

    @Column(name = "profile_completion", nullable = false)
    private int profileCompletion = 20;

    @Column(name = "tenth_percentage", precision = 5, scale = 2)
    private BigDecimal tenthPercentage;

    @Column(name = "twelfth_percentage", precision = 5, scale = 2)
    private BigDecimal twelfthPercentage;

    @Column(name = "physics_marks", precision = 5, scale = 2)
    private BigDecimal physicsMarks;

    @Column(name = "mathematics_marks", precision = 5, scale = 2)
    private BigDecimal mathematicsMarks;

    @Column(name = "english_marks", precision = 5, scale = 2)
    private BigDecimal englishMarks;

    @Column(name = "graduation_details", length = 250)
    private String graduationDetails;

    @Column(name = "medical_status", length = 40)
    private String medicalStatus;

    @Column(name = "flying_experience", length = 250)
    private String flyingExperience;

    @Column(name = "total_flight_hours")
    private Integer totalFlightHours;

    @Column(name = "previous_aviation_training", length = 250)
    private String previousAviationTraining;

    @Column(name = "english_proficiency", length = 40)
    private String englishProficiency;

    @Column(name = "passport_available")
    private Boolean passportAvailable;

    @Column(name = "preferred_program", length = 120)
    private String preferredProgram;

    @Column(name = "preferred_training_location", length = 120)
    private String preferredTrainingLocation;

    @Column(name = "preferred_airline", length = 120)
    private String preferredAirline;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    protected CandidateProfile() {
    }

    public CandidateProfile(
            UserAccount user,
            String phone,
            LocalDate dateOfBirth,
            String nationality,
            String city,
            String state,
            String country) {
        this.user = user;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public UserAccount getUser() {
        return user;
    }

    public String getPhone() {
        return phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public int getProfileCompletion() {
        return profileCompletion;
    }

    public BigDecimal getTenthPercentage() {
        return tenthPercentage;
    }

    public BigDecimal getTwelfthPercentage() {
        return twelfthPercentage;
    }

    public BigDecimal getPhysicsMarks() {
        return physicsMarks;
    }

    public BigDecimal getMathematicsMarks() {
        return mathematicsMarks;
    }

    public BigDecimal getEnglishMarks() {
        return englishMarks;
    }

    public String getGraduationDetails() {
        return graduationDetails;
    }

    public String getMedicalStatus() {
        return medicalStatus;
    }

    public String getFlyingExperience() {
        return flyingExperience;
    }

    public Integer getTotalFlightHours() {
        return totalFlightHours;
    }

    public String getPreviousAviationTraining() {
        return previousAviationTraining;
    }

    public String getEnglishProficiency() {
        return englishProficiency;
    }

    public Boolean getPassportAvailable() {
        return passportAvailable;
    }

    public String getPreferredProgram() {
        return preferredProgram;
    }

    public String getPreferredTrainingLocation() {
        return preferredTrainingLocation;
    }

    public String getPreferredAirline() {
        return preferredAirline;
    }

    public LocalDate getAvailableFrom() {
        return availableFrom;
    }

    public void updateDetails(CandidateProfileUpdate update) {
        this.phone = update.phone().trim();
        this.nationality = update.nationality().trim();
        this.city = update.city().trim();
        this.state = update.state().trim();
        this.country = update.country().trim();
        this.tenthPercentage = update.tenthPercentage();
        this.twelfthPercentage = update.twelfthPercentage();
        this.physicsMarks = update.physicsMarks();
        this.mathematicsMarks = update.mathematicsMarks();
        this.englishMarks = update.englishMarks();
        this.graduationDetails = normalize(update.graduationDetails());
        this.medicalStatus = update.medicalStatus();
        this.flyingExperience = normalize(update.flyingExperience());
        this.totalFlightHours = update.totalFlightHours();
        this.previousAviationTraining = normalize(update.previousAviationTraining());
        this.englishProficiency = update.englishProficiency();
        this.passportAvailable = update.passportAvailable();
        this.preferredProgram = normalize(update.preferredProgram());
        this.preferredTrainingLocation = normalize(update.preferredTrainingLocation());
        this.preferredAirline = normalize(update.preferredAirline());
        this.availableFrom = update.availableFrom();
        this.profileCompletion = calculateCompletion();
    }

    private int calculateCompletion() {
        int completed = 20;
        Object[] optionalValues = {
                tenthPercentage, twelfthPercentage, physicsMarks, mathematicsMarks, englishMarks,
                graduationDetails, medicalStatus, flyingExperience, totalFlightHours,
                previousAviationTraining, englishProficiency, passportAvailable,
                preferredProgram, preferredTrainingLocation, preferredAirline, availableFrom
        };
        for (Object value : optionalValues) {
            if (value != null && (!(value instanceof String text) || !text.isBlank())) {
                completed += 5;
            }
        }
        return Math.min(completed, 100);
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }
}

