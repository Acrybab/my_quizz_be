package com.devquiz.quizlet_backend.group.service.impl;

import com.devquiz.quizlet_backend.group.dto.Response.GroupMemberReponse;
import com.devquiz.quizlet_backend.group.dto.Response.UserMinimalResponse;
import com.devquiz.quizlet_backend.group.entity.GroupMember;
import com.devquiz.quizlet_backend.group.entity.StudyGroup;
import com.devquiz.quizlet_backend.group.repository.GroupMemberRepository;
import com.devquiz.quizlet_backend.group.repository.StudyGroupRepository;
import com.devquiz.quizlet_backend.group.service.GroupMemberService;
import com.devquiz.quizlet_backend.notification.service.NotificationService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMemberServiceImpl implements GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;
    private final UserRepository userRepository;
    private final StudyGroupRepository studyGroupRepository;
    private final NotificationService notificationService;
    @Override
    public ResponseEntity<ApiResponse<GroupMember>> addMemberToGroup(Long userId, Long groupId, String role) {

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        StudyGroup studyGroup = studyGroupRepository.findById(groupId).orElseThrow(() -> new RuntimeException("StudyGroup not found with id: " + groupId));
        GroupMember groupMember = groupMemberRepository.findByUserIdAndGroupId(userId, groupId);
        if (groupMember != null) {
            throw new RuntimeException("User is already a member of the group");
        }

        GroupMember newGroupMember = new GroupMember();
        newGroupMember.setGroup(studyGroup);
        newGroupMember.setUser(user);
        newGroupMember.setRole(role);

        GroupMember savedGroupMember = groupMemberRepository.save(newGroupMember);

        String title = "Bạn có nhóm mới!";
        String msg = String.format("Bạn đã được thêm vào nhóm '%s'", studyGroup.getStudyGroupName());
        String targetUrl = "/study-group/" + groupId;
        notificationService .createAndSendNotification(user , title, msg,targetUrl );
        return ResponseEntity.ok(new ApiResponse<>(200, "User added to group successfully", savedGroupMember));

    }

    @Override
    public ResponseEntity<ApiResponse<List<GroupMember>>> getAllMembers(Long groupId) {
        List<GroupMember> groupMembers = groupMemberRepository.findByGroupId(groupId);

         return ResponseEntity.ok(new ApiResponse<>(200, "Members retrieved successfully", groupMembers));

    }
}
