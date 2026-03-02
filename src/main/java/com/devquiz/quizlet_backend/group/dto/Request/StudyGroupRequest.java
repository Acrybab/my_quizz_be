package com.devquiz.quizlet_backend.group.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroupRequest {
    @NotBlank(message = "Study group name cannot be blank")
    private String studyGroupName;

    private Long studySetId; // ID của study set để thêm vào nhóm
    private Long userId; // ID của người dùng để thêm vào nhóm
}
