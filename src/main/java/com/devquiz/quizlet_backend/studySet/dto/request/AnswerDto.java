package com.devquiz.quizlet_backend.studySet.dto.request;

import lombok.Data;

@Data
public class AnswerDto {
    private long cardId;
    private String userAnswer;
    private String displayedDef;
}
