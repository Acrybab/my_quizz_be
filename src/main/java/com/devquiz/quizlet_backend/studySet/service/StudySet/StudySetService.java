package com.devquiz.quizlet_backend.studySet.service.StudySet;

import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;

import java.util.List;

public interface StudySetService {
StudySetResponse createStudySet(StudySetRequest studySetRequest);
List<StudySetResponse> getAllStudySets();
StudySetResponse getStudySetById(Long id);
StudySetResponse updateStudySet(Long id, StudySetRequest studySetRequest);
void deleteStudySet(Long id);

}
