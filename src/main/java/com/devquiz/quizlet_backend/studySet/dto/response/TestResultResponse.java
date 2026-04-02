package com.devquiz.quizlet_backend.studySet.dto.response;

import com.devquiz.quizlet_backend.studySet.dto.request.AnswerDto;
import com.devquiz.quizlet_backend.studySet.entity.TestResultDetails;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestResultResponse {
   private Long studySetId;
   private Double score;
   private Integer totalQuestions;
   private Integer correctAnswers;
   private Long testResultId;
   private String testMode;
   private UserResponse user;
   private StudySetResponse studySet;
   private Integer completionTime;
   private LocalDateTime createdAt;
   private List<TestResultDetailResponse> details;
}
