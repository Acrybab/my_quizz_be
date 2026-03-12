package com.devquiz.quizlet_backend.studySet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestionDto {
    private Long cardId;
    private TestType testType;
    private String question;
    private List<String> options;
    private String placeHolder;
    private String showDefinitions;
    private String correctAnswer;
}

