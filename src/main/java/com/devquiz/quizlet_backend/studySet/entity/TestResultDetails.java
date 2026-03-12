package com.devquiz.quizlet_backend.studySet.entity;

import com.devquiz.quizlet_backend.card.entity.Card;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "test_result_details")
public class TestResultDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long testResultDetailsId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_result_id", nullable = false)
    private TestResult testResult;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;
    private String userAnswer;
    private boolean isCorrect;
}
