package com.devquiz.quizlet_backend.learn.dto.response;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuizzDataReponse {
    private List<QuizQuestionResponse> questions;
//    private String cardStatus;
      // Các thông số để FE vẽ Progress Bar khi vừa load trang
    private long totalCards;
    private long masteredCount;
    private double initialPercentage;
}
