package com.devquiz.quizlet_backend.group.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMemberRequest {
    private Long userId;
    private Long groupId;
    private String role; // Ví dụ: "MEMBER", "MODERATOR"
}
