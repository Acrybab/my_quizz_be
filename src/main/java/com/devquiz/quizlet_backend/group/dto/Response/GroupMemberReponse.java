package com.devquiz.quizlet_backend.group.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberReponse {
    private Long groupMemberId;
    private UserMinimalResponse user;
    private Long groupId;
    private String role;
    private String groupName;
}
