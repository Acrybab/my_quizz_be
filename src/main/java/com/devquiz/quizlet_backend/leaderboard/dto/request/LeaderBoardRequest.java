package com.devquiz.quizlet_backend.leaderboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderBoardRequest {
    private Long userId;
    private int currentScore;
    private Double currentTime;
    private Long studySetId;
}
