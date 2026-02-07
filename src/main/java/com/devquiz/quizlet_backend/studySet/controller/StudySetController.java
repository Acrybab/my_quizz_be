package com.devquiz.quizlet_backend.studySet.controller;

import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;

import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.service.StudySet.StudySetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/study-sets")
@RequiredArgsConstructor
public class StudySetController {
    private final StudySetService studySetService;
    @PostMapping("/create")
    public StudySetResponse createStudySet(@RequestBody StudySetRequest studySet ) {

        return studySetService.createStudySet(studySet);
    }

     @GetMapping("")
    public List<StudySetResponse> getAllStudySets() {
         return studySetService.getAllStudySets();
     }

     @GetMapping("/{id}")
    public StudySetResponse getStudySetById(@PathVariable Long id) {
         return studySetService.getStudySetById(id);
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
