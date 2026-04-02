package com.devquiz.quizlet_backend.studySet.dto.response;

import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestResultDetailResponse {
    private String userAnswer;
    private CardResultResponse card;
    private Boolean isCorrect;

}
