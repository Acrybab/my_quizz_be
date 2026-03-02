package com.devquiz.quizlet_backend.group.controller;

import com.devquiz.quizlet_backend.group.dto.Request.StudyGroupRequest;
import com.devquiz.quizlet_backend.group.dto.Response.StudyGroupResponse;
import com.devquiz.quizlet_backend.group.service.StudyGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<StudyGroupResponse>> getAllStudyGroups() {
        return ResponseEntity.ok(studyGroupService.getAllStudyGroups());
    }
}
