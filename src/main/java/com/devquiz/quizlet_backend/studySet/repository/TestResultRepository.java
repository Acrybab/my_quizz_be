package com.devquiz.quizlet_backend.studySet.repository;

import com.devquiz.quizlet_backend.studySet.dto.response.TestResultResponse;
import com.devquiz.quizlet_backend.studySet.entity.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult,Long> {
    @Query("SELECT tr FROM TestResult tr JOIN FETCH tr.user u JOIN FETCH tr.studySet s JOIN TestResultDetails trd  ON tr.testResultId = trd.testResult.testResultId  WHERE s.studySetId = :studySetId AND u.email = :userEmail")
    List<TestResult> findByStudySetIdAndUserEmail(Long studySetId, String userEmail);
}
