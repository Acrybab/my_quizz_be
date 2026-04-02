package com.devquiz.quizlet_backend.studySet.entity;

import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_result")
public class TestResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testResultId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
//    @JsonIgnore
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
    @JoinColumn(name = "study_set_id", nullable = false)
    private StudySet studySet;
    private Double score;
    private Integer correctAnswers;
    @Column(nullable = true)
    private Integer totalQuestions;
    private String testMode;
    private Integer completionTime;
    @OneToMany(mappedBy = "testResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestResultDetails> details = new ArrayList<>();
    private LocalDateTime createdTestResultAt = LocalDateTime.now();
}
