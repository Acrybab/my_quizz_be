package com.devquiz.quizlet_backend.studySet.repository;

import com.devquiz.quizlet_backend.studySet.entity.TestResultDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestResultDetailRepository extends JpaRepository<TestResultDetails, Long> {
}
