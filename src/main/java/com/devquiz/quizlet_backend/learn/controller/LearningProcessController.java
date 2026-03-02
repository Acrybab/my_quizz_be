package com.devquiz.quizlet_backend.learn.controller;

import com.devquiz.quizlet_backend.learn.dto.request.LearningRequest;
import com.devquiz.quizlet_backend.learn.dto.response.LearningProgressResponse;
import com.devquiz.quizlet_backend.learn.service.LearningService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/api/v1/learning")
@RequiredArgsConstructor
public class LearningProcessController {
 private final LearningService learningService;

    @PostMapping("/update-progress")
    public ResponseEntity<ApiResponse<LearningProgressResponse>> updateProgress(
            @RequestBody LearningRequest learningRequest) {

        // Gọi service và nhận về object chứa các con số (total, mastered, percentage)
        LearningProgressResponse progress = learningService.updateLearingProcess(learningRequest);

        return ResponseEntity.ok(
                ApiResponse.<LearningProgressResponse>builder()
                        .code(200)
                        .message("Cập nhật tiến độ học tập thành công")
                        .data(progress) // Trả về data gồm total, mastered, percentage
                        .build()
        );
    }
    @GetMapping("/progress-reset/{studySetId}")
    public ResponseEntity<ApiResponse<String>> resetProgress(Principal principal , @PathVariable Long studySetId) {
        String username = principal.getName();
        learningService.resetLearningProcees(username ,studySetId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .code(200)
                        .message("Đặt lại tiến độ học tập thành công")
                        .data("Progress reset successfully") // Có thể trả về thông điệp hoặc null
                        .build()
        );

    }
}
