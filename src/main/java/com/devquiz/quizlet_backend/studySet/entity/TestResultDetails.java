package com.devquiz.quizlet_backend.studySet.entity;

import com.devquiz.quizlet_backend.card.entity.Card;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "test_result_details")
public class TestResultDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testResultDetailsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_result_id", nullable = false)
    @JsonIgnore
    private TestResult testResult;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    @JsonIgnore
    private Card card;
    private String userAnswer;
    private boolean isCorrect;
}
