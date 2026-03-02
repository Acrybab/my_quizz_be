package com.devquiz.quizlet_backend.learn.dto.request;

import com.devquiz.quizlet_backend.learn.types.LearningStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LearningRequest {
@NotBlank(message = "User ID is required")
    private Long userId;
//
//    @NotBlank(message = "Study Set ID is required")
//    private Long studySetId;

    @NotBlank(message = "Learning status is required")
    private LearningStatus status;

    @NotBlank(message = "Card ID is required")
    private Long cardId;

    @NotBlank(message = "Current index is required")
    private Integer currentIndex;

    private Long studySetId;

//    private Card card;
//    private User user;
}
