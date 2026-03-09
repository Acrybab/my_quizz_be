package com.devquiz.quizlet_backend.group.service;

import com.devquiz.quizlet_backend.group.dto.Request.StudyGroupRequest;
import com.devquiz.quizlet_backend.group.dto.Response.StudyGroupResponse;
import com.devquiz.quizlet_backend.group.entity.StudyGroup;
import com.devquiz.quizlet_backend.group.repository.GroupMemberRepository;
import com.devquiz.quizlet_backend.group.repository.StudyGroupRepository;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.repository.StudySetRepository;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyGroupService {
    public final StudyGroupRepository studyGroupRepository;
    public final UserRepository userRepository;
    public final StudySetRepository studySetRepository;
    public final GroupMemberService  groupMemberService;
public StudyGroupResponse createStudyGroup(StudyGroupRequest studyGroupRequest) {
    User user = userRepository.findById(studyGroupRequest.getUserId()).orElseThrow(
            () -> new RuntimeException("User not found with id: " + studyGroupRequest.getUserId())
    );
    StudySet studySet = studySetRepository.findById(studyGroupRequest.getStudySetId()).orElseThrow(
            () -> new RuntimeException("StudySet not found with id: " + studyGroupRequest.getStudySetId())
    );
    StudyGroup studyGroup = new StudyGroup();
    studyGroup.setStudyGroupName(studyGroupRequest.getStudyGroupName());
    studyGroup.setAdmin(user);

    // 3. Khởi tạo list bên trong nếu nó null (Tránh NullPointerException)
    if (studyGroup.getStudySets() == null) {
        studyGroup.setStudySets(new ArrayList<>());
    }
    studyGroup.getStudySets().add(studySet);

    // 4. Lưu và chuyển đổi sang Response
    StudyGroup savedGroup = studyGroupRepository.save(studyGroup);
    groupMemberService.addMemberToGroup(savedGroup.getAdmin().getUserId() , savedGroup.getGroupId(), "ADMIN"); // Thêm admin vào nhóm


    return mapToResponse(savedGroup) ;
}


public List<StudyGroupResponse> getAllStudyGroups(String userEmail) {
    User user = userRepository.findByEmail(userEmail).orElseThrow(
            () -> new RuntimeException("User not found with email: " + userEmail)
    );
    List<StudyGroup> studyGroups = studyGroupRepository.findByMemberUserId(user.getUserId());
    List<StudyGroupResponse> responses = new ArrayList<>();
    for (StudyGroup group : studyGroups) {
        responses.add(mapToResponse(group));
    }
    return responses;
}

public StudyGroupResponse getStudyGroupById(Long id) {
    StudyGroup group = studyGroupRepository.findById(id).orElseThrow(
            () -> new RuntimeException("StudyGroup not found with id: " + id)
    );
    return mapToResponse(group);
}



    private StudyGroupResponse mapToResponse(StudyGroup group) {
        // Giả sử bạn dùng Builder hoặc tạo thủ công
        return StudyGroupResponse.builder()
                .id(group.getGroupId())
                .studyGroupName(group.getStudyGroupName())
                .adminName(group.getAdmin().getFirstName() + " " + group.getAdmin().getLastName())
                .studySets(group.getStudySets())
                .build();
    }



}
