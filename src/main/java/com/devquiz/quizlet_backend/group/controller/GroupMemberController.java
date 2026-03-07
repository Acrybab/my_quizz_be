package com.devquiz.quizlet_backend.group.controller;

import com.devquiz.quizlet_backend.group.dto.Request.GroupMemberRequest;

import com.devquiz.quizlet_backend.group.service.GroupMemberService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/group-members")

@RequiredArgsConstructor // <--- THÊM CÁI NÀY ĐỂ LOMBOK TỰ TẠO CONSTRUCTOR
public class GroupMemberController {
    private final GroupMemberService groupMemberService;

    @PostMapping("/add")
    public ResponseEntity<?> addMemberToGroup(@RequestBody GroupMemberRequest request) {
        return groupMemberService.addMemberToGroup(request.getUserId(), request.getGroupId(), request.getRole());
    }
    @GetMapping("{groupId}/members")
    public ResponseEntity<?> getAllMembers(@PathVariable Long groupId) {
        return groupMemberService.getAllMembers(groupId);
    }

}
