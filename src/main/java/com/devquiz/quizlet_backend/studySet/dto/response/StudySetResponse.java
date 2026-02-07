package com.devquiz.quizlet_backend.studySet.dto.response;

import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.user.dto.response.UserResponse;
import com.devquiz.quizlet_backend.user.dto.response.UserResponseStudySet;
import com.devquiz.quizlet_backend.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudySetResponse {
    private String title;
    private String description;
    private String coverImage;
    private boolean isPublic;
    private List<CardResponse> cards;
    private String userName;
    private String avatarUrl;
}
