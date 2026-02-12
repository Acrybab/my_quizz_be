package com.devquiz.quizlet_backend.studySet.controller;

import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;

import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.service.StudySet.StudySetService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/study-sets")
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService studySetService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/create")
    public ResponseEntity<ApiResponse<StudySetResponse>> create(@ModelAttribute StudySetRequest request) {
        StudySetResponse data = studySetService.createStudySet(request);

        // Bọc dữ liệu vào ApiResponse
        ApiResponse<StudySetResponse> response = new ApiResponse<>();
        response.setData(data);

        return ResponseEntity.ok(response);
    }

     @GetMapping("")
    public ResponseEntity<ApiResponse<List<StudySetResponse>>> getAllStudySets() {
         ApiResponse<List<StudySetResponse>> response = ApiResponse.<List<StudySetResponse>>builder()
                 .code(200)
                 .message("Lấy danh sách Study Sets thành công")
                 .data(studySetService.getAllStudySets())
                 .build();
            return ResponseEntity.ok(response);
     }

     @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudySetResponse>> getStudySetById(@PathVariable Long id) {
         return ResponseEntity.ok(
                 ApiResponse.<StudySetResponse>builder()
                         .code(200)
                         .message("Lấy Study Set thành công")
                         .data(studySetService.getStudySetById(id))
                         .build()
         );
     }
     @PutMapping ("/{id}")
    public StudySetResponse updateStudySet(@PathVariable Long id, @RequestBody StudySetRequest studySetRequest) {
         return studySetService.updateStudySet(id, studySetRequest);
     }

     @DeleteMapping("/{id}")
    public void deleteStudySet(@PathVariable Long id) {
         studySetService.deleteStudySet(id);
     }

}
