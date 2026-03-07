package com.devquiz.quizlet_backend.group.service;

import com.devquiz.quizlet_backend.group.dto.Response.GroupMemberReponse;
import com.devquiz.quizlet_backend.group.entity.GroupMember;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface GroupMemberService {
     ResponseEntity<ApiResponse<GroupMember>> addMemberToGroup(Long userId, Long groupId, String role);
     ResponseEntity<ApiResponse<List<GroupMember>>> getAllMembers(Long groupId);
}
