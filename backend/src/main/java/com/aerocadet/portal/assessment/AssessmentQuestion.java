package com.aerocadet.portal.assessment;

import com.aerocadet.portal.program.CadetProgram;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assessment_questions")
public class AssessmentQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "program_id")
    private CadetProgram program;
    @Column(nullable = false, length = 40) private String category;
    @Column(nullable = false, columnDefinition = "TEXT") private String question;
    @Column(name = "option_a", nullable = false, length = 300) private String optionA;
    @Column(name = "option_b", nullable = false, length = 300) private String optionB;
    @Column(name = "option_c", nullable = false, length = 300) private String optionC;
    @Column(name = "option_d", nullable = false, length = 300) private String optionD;
    @Column(name = "correct_option", nullable = false, length = 1) private String correctOption;
    @Column(nullable = false) private int points;
    @Column(nullable = false) private boolean active = true;
    protected AssessmentQuestion() {}
    public Long getId() { return id; }
    public String getCategory() { return category; }
    public String getQuestion() { return question; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectOption() { return correctOption; }
    public int getPoints() { return points; }
}

