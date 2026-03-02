package com.devquiz.quizlet_backend.group.repository;

import com.devquiz.quizlet_backend.group.entity.StudyGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
}
