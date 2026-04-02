package com.devquiz.quizlet_backend.studySet.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class TestSubmissionDto {
    private Long studySetId;
    private Integer totalQuestions;
    private List<AnswerDto> answers;
}
