package com.devquiz.quizlet_backend.learn.dto.response;

import lombok.*;

import java.util.List;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionResponse {
    private Long cardId;
    private String term;
    private String definition;
    private List<String>options;
    private String cardStatus;

}
