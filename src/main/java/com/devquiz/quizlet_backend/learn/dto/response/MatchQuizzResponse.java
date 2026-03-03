package com.devquiz.quizlet_backend.learn.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchQuizzResponse {
    private Long cardId;
    private String term;
    private String definition;
    private String type;
}
