package com.devquiz.quizlet_backend.leaderboard.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeaderBoardRequest {
    private Long userId;
    private int currentScore;
    private Long studySetId;
    private Double bestTime;
    private Integer attempts;
    private LocalDateTime lastPlayedAt;
}
