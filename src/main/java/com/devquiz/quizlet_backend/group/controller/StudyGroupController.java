package com.devquiz.quizlet_backend.group.controller;

import com.devquiz.quizlet_backend.group.dto.Request.StudyGroupRequest;
import com.devquiz.quizlet_backend.group.dto.Response.StudyGroupResponse;
import com.devquiz.quizlet_backend.group.service.StudyGroupService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/api/v1/study-groups")
@RequiredArgsConstructor
public class StudyGroupController {
    private final StudyGroupService studyGroupService;
    @PostMapping("/create")
    public ResponseEntity<StudyGroupResponse> createStudyGroup(@RequestBody StudyGroupRequest studyGroupRequest) {
        return ResponseEntity.ok(studyGroupService.createStudyGroup(studyGroupRequest));



    }
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<StudyGroupResponse>>> getAllStudyGroups(Principal principal) {

        List<StudyGroupResponse> studyGroupResponse = studyGroupService.getAllStudyGroups(principal.getName());

        return ResponseEntity.ok(ApiResponse.<List<StudyGroupResponse>>builder()
                .code(1000)
                .message("Get all study groups successfully")
                .data(studyGroupResponse)
                .build()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyGroupResponse>> getStudyGroupById(@PathVariable Long id) {
        StudyGroupResponse studyGroupResponse = studyGroupService.getStudyGroupById(id);

        return ResponseEntity.ok(ApiResponse.<StudyGroupResponse>builder()
                .code(1000)
                .message("Get study group by id successfully")
                .data(studyGroupResponse)
                .build()
        );
    }
}
