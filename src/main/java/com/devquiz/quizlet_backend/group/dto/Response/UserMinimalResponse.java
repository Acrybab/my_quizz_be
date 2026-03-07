package com.devquiz.quizlet_backend.group.dto.Response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMinimalResponse {
    private Long userId;
    private String  firstName;
    private String lastName;
    private String avatarUrl;
}
