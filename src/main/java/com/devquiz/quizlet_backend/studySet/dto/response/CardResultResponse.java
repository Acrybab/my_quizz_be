package com.devquiz.quizlet_backend.studySet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardResultResponse {
    private Long cardId;
    private String term;
    private String definition;
}
