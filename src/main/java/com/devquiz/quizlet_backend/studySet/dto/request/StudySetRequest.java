package com.devquiz.quizlet_backend.studySet.dto.request;

import com.devquiz.quizlet_backend.card.dto.request.CardRequest;
import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudySetRequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(min=3, max=100, message = "Title must be between 3 and 100 characters")
    private String title;
    @Size(max=500, message = "Description cannot exceed 500 characters")
    private String description;
    private MultipartFile coverImage;
//    private MultipartFile file;
    @NotEmpty(message = "Study set must have at least one card")
    private List<CardRequest> cards;
    private String userEmail;
}
