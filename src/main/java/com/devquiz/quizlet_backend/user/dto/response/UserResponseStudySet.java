package com.devquiz.quizlet_backend.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseStudySet {
    private Long id;
    private String userName;
    private String email;
    private String avatarUrl;

}
